#include <stdio.h>
#include <stdlib.h>
#include <memory.h>

int iW, iH;

unsigned char* readImage(char* filename, int* iW, int* iH) 
{
  FILE* f;
  char comment[256];
  int maxval;
  unsigned char* image;

  if ((f = fopen(filename,"r")) == 0)
    return 0;
  fscanf(f,"P5");
  while (fscanf(f," #%[^\n]\n",comment) == 1);
  if (fscanf(f,"%d",iW) != 1)
    return 0;
  while (fscanf(f," #%[^\n]\n",comment) == 1);
  if (fscanf(f,"%d",iH) != 1)
    return 0;
  while (fscanf(f," #%[^\n]\n",comment) == 1);    
  if (fscanf(f,"%d",&maxval) != 1)
    return 0;
  fread(comment,1,1,f); /* ONE whitespace after maxval */
  
  image = (unsigned char*)calloc(*iW * *iH,1);
  if (fread(image,1,*iW * *iH,f) != (size_t)*iW * *iH) {
    free(image);
    image = 0;
  }
  fclose(f);
  return image;
}

void scan(unsigned char *image, char ch, int height, int width,
          unsigned char *bits)
{
  int x2,y2,bitPos,b;
  int nBits,nBytes;
  int x = (ch&0xff)%32;
  int y = (ch&0xff)/32-1;
  if (y>3) y--;
  x *= width;
  y *= height;
  nBits = height*width*2;
  nBytes = (nBits+7)/8;
  memset(bits,0,nBytes);
  for (y2=0; y2<height; y2++)
    for (x2=0; x2<width; x2++) {
      bitPos = (y2*width+x2)*2;
      b = (image[(y+y2)*iW+x+x2]+85/2)/85;
      bits[bitPos/8] |= b<<(bitPos&7);
    }
}

void getBaselinePosition(unsigned char *image, char ch, int height, int width,
                         int *baseline_position)
{
  int x2,y2,b;
  int x = (ch&0xff)%32;
  int y = (ch&0xff)/32-1;
  if (y>3) y--;
  x *= width;
  y *= height;
  x2 = width/2;
  for (y2=0; y2<height; y2++) {
    b = (image[(y+y2)*iW+x+x2]+85/2)/85;
    if (b==3)
      *baseline_position = y2+1;
  }
}

void getOverline(unsigned char *image, char ch, int height, int width,
                 int *overline_thickness)
{
  int x2,y2,b;
  int x = (ch&0xff)%32;
  int y = (ch&0xff)/32-1;
  if (y>3) y--;
  x *= width;
  y *= height;
  x2 = width-1;
  for (y2=0; y2<height; y2++) {
    b = (image[(y+y2)*iW+x+x2]+85/2)/85;
    if (b==3)
      *overline_thickness = y2+1;
  }
}

int vLineSum(unsigned char *image, char ch, int height, int width, int x2)
{
  int y2,sum;
  int x = (ch&0xff)%32;
  int y = (ch&0xff)/32-1;
  if (y>3) y--;
  x *= width;
  y *= height;
  for (y2=0,sum=0; y2<height; y2++)
    sum += (image[(y+y2)*iW+x+x2]+85/2)/85;
  return sum;
}

int italicPenalty(unsigned char *image, char ch, int height, int width,
                  int xOff, int charWidth, int italicOffset) 
{
  int x2,y2,off,off1,b,b2,sum;
  int x = (ch&0xff)%32;
  int y = (ch&0xff)/32-1;
  if (y>3) y--;
  x *= width;
  y *= height;
  sum = 0;
  for (y2=0; y2<height; y2++) {
    off = -height/4+y2/2+italicOffset;
    off1 = y2&1;
    for (x2 = -off-off1; x2 < charWidth-off; x2++) {
      if (x2 + off >= 0 && x2 + off < charWidth) {
        b = (image[(y+y2)*iW+x+x2+off]+85/2)/85;
      } else {
        b = 0;
      }
      if (x2 + off + off1 >= 0 && x2 + off + off1 < charWidth) {
        b2 = (image[(y+y2)*iW+x+x2+off+off1]+85/2)/85;
      } else {
        b2 = 0;
      }
      b = (b+b2)/2;
      if (x2 < xOff) {
        sum += b;
      } else if (x2 >= xOff+charWidth) {
        sum -= b;
      }
    }
  }
  return abs(sum);
}

void getItalicOffset(unsigned char *image, char ch, int height, int width,
                     int xOff, int charWidth, unsigned char *italicOffset)
{
  int bestPenalty = 10000;
  int i;
  for (i=-height/4; i<=height/4; i++) {
    int penalty = italicPenalty(image,ch,height,width,xOff,charWidth,i);
    if (penalty < bestPenalty ||
        (penalty == bestPenalty && abs(i)<abs(*italicOffset))) {
      bestPenalty = penalty;
      *italicOffset = i;
    }
  }
}

void getSpace(unsigned char *image, char ch, int height, int width,
              int *left, int *right)
{
  for (*left =0; vLineSum(image,ch,height,width,        *left )==0;(*left)++ );
  for (*right=0; vLineSum(image,ch,height,width,width-1-*right)==0;(*right)++);
}

void getWidth(unsigned char *image, char ch, int height, int width,
              int space_left, int space_right, unsigned char* xOff,
              unsigned char* charWidth)
{
  int x, sum, sum2;
  if (ch == ' ') ch = '.'; // Space should be as wide as a dot

  *xOff = 0;
  for (x=0,sum=0; x<space_left; x++)
    sum += vLineSum(image,ch,height,width,x);
  if (sum == 0) {
    for (x=space_left; x<width; x+=2) {
      sum  = vLineSum(image,ch,height,width,x);
      sum2 = vLineSum(image,ch,height,width,x+1);
      if (sum == 0 && sum2 < 6) {
        *xOff = (x+2-space_left)/2;
      } else {
        break;
      }
    }
  }
  *charWidth = width/2 - *xOff;
  for (x=0,sum=0; x<space_right; x++)
    sum += vLineSum(image,ch,height,width,width-1-x);
  if (sum == 0) {
    for (x=space_right; x<width - *xOff*2; x+=2) {
      sum  = vLineSum(image,ch,height,width,width-1-x);
      sum2 = vLineSum(image,ch,height,width,width-1-(x+1));
      if (sum == 0 && sum2 < 6) {
        *charWidth = (width - (x+2-space_right))/2 - *xOff;
      } else {
        break;
      }
    }
  }
}

int printStringChar(unsigned char b) 
{
  switch (b) {
    case 8:  printf("\\b");  return 2;
    case 9:  printf("\\t");  return 2;
    case 10: printf("\\n");  return 2;
    case 12: printf("\\f");  return 2;
    case 13: printf("\\r");  return 2;
    case 34: printf("\\\""); return 2;
    case 39: printf("\\'");  return 2;
    case 92: printf("\\\\"); return 2;
    default:
      if (b>=32 && b<=126) {
        printf("%c",b);
        return 1;
      } else {
        printf("\\%03o",b);
        return 4;
      }
  }
}

int main(int argc, char *argv[])
{
  unsigned char* image;
  int c,nBytes;
  unsigned char *bits;
  int char_height, char_width;
  int baseline_position, overline_thickness, space_left, space_right;
  char *prefix;
  char *charSet = " !#%'()*+,-./0123456789:<=>?ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_"
    "abcdefghijklmnopqrstuvwxyz¡£«­°¹²³µ¶·»¼¿ÅÐØÞßãëð";
  unsigned char *charXOff = (unsigned char*)malloc(strlen(charSet));
  unsigned char *charWidth = (unsigned char*)malloc(strlen(charSet));
  unsigned char *charItalicOffset = (unsigned char*)malloc(strlen(charSet));
  FILE *f;

  if (argc<4 || (image=readImage(argv[1],&iW,&iH))==0 || iW%32!=0 || iH%6!=0) {
    printf("Usage: %s image.pgm prefix out.dat > out.java\n",argv[0]);
    exit(1);
  }
  prefix = argv[2];
  
  char_width = iW/32;
  char_height = iH/6;
  getBaselinePosition(image,'E',char_height,char_width,&baseline_position);
  getOverline(image,'¿',char_height,char_width,&overline_thickness);
  getSpace(image,'H',char_height,char_width,&space_left,&space_right);

  nBytes = (char_height*char_width*2+7)/8;
  bits = (unsigned char*)malloc(nBytes);

  f = fopen(argv[3],"w");
  for (c=0; c<(int)strlen(charSet); c++) {
    scan(image,charSet[c],char_height,char_width,bits);
    getWidth(image,charSet[c],char_height,char_width,space_left,space_right,
             &charXOff[c],&charWidth[c]);
    getItalicOffset(image,charSet[c],char_height,char_width,
                    charXOff[c],charWidth[c],&charItalicOffset[c]);
    fwrite(bits,1,nBytes,f);
    fflush(f);
  }
  fclose(f);

  printf("    static final String %sCharBitsResource = \"/%s\";\n",prefix,
         argv[3]);
  printf("    static final int %sCharMaxWidth = %d;\n",prefix,char_width/2);
  printf("    static final int %sCharHeight = %d;\n",prefix,char_height);
  printf("    static final int %sBaselinePosition = %d;\n",prefix,baseline_position);
  printf("    static final int %sOverlineThickness = %d;\n",prefix,overline_thickness);
  printf("    static final String %sCharSet =\n        \"",prefix);
  for (c=0; c<(int)strlen(charSet); c++)
    printStringChar(charSet[c]);
  printf("\";\n");
  printf("    static final String %sCharXOff =\n        \"",prefix);
  for (c=0; c<(int)strlen(charSet); c++)
    printStringChar(charXOff[c]);
  printf("\";\n");
  printf("    static final String %sCharWidth =\n        \"",prefix);
  for (c=0; c<(int)strlen(charSet); c++)
    printStringChar(charWidth[c]);
  printf("\";\n");
  printf("    static final String %sCharItalicOffset =\n        \"",prefix);
  for (c=0; c<(int)strlen(charSet); c++)
    printStringChar(charItalicOffset[c]);
  printf("\";\n\n");
  return 0;
}
