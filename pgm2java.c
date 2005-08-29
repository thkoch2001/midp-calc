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

void scan(unsigned char *image, int x, int y, int height, int width,
          unsigned char *bits)
{
  int x2,y2,bitPos,b;
  int nBits,nBytes;
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
  int c,i,j,k;
  unsigned char bits[32*3][100]; // Works for size up to 20x20
  int char_height, char_width;
  char *prefix;
  char *charSet = " \"$%&'*+,-./0123456789:=>ABCDEFMR[]_aefilmnoprtvxy";
  FILE *f;
  // Strings written with font
  //
  // 10000000_
  // -1'234.5+6.78e9i
  // /0A,BCDE,F000
  // nan inf *****
  // n= $x= $x"= $y= $y"= $xy= $&x= $&"x= $&y= $&"y= $x&y= $y&x= $&x&y=
  // pv= fv= np= pmt= ir%=
  // M0= M1=
  // R0= R1> Col:1 M:[3x4] no matrix

  if (argc<4 || (image=readImage(argv[1],&iW,&iH))==0 || iW%32!=0 || iH%3!=0) {
    printf("Usage: %s image.pgm prefix out.dat > out.java\n",argv[0]);
    exit(1);
  }
  prefix = argv[2];
  
  char_width = iW/32;
  char_height = iH/3;
  for (i=0; i<3; i++)
    for (j=0; j<32; j++)
      scan(image,j*char_width,i*char_height,char_height,char_width,
           bits[i*32+j]);

  f = fopen(argv[3],"w");
  for (c=0; c<(int)strlen(charSet); c++) {
    i = charSet[c]/32-1;
    j = charSet[c]%32;
    int nBytes = (char_height*char_width*2+7)/8;
    for (k=0; k<nBytes; k++) {
      unsigned char b = bits[i*32+j][k];
      fputc(b,f);
    }
  }
  fclose(f);

  printf("  protected final String %schar_bits_resource = \"/%s\";\n",prefix,
         argv[3]);
  printf("  protected final int %schar_width = %d;\n",prefix,char_width/2);
  printf("  protected final int %schar_height = %d;\n",prefix,char_height);
  printf("  protected final String %schar_set =\n    \"",prefix);
  for (c=0; c<(int)strlen(charSet); c++)
    printStringChar(charSet[c]);
  printf("\";\n");
  return 0;
}
