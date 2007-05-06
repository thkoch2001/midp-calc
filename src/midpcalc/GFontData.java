package midpcalc;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

class GFontData {
    
    private int charHeight, charMaxWidth;

    private byte[] charBits;

    private String charSet;

    byte[] charWidth;
    byte[] charXOff;

    private boolean bgr;

    private boolean sizeX2;

    private Image charCache;

    private Graphics charCacheGraphics;

    private boolean largeCache;
    
    private int canvasWidth, canvasHeight;

    private int cacheWidth, cacheHeight, cacheSize;

    private int bucketSize;

    private short[] cacheHash;

    private short[] cacheTime;

    private short time;
    
    private int refCount;
    private int largeCacheRefCount;

    private static GFontData smallFontData;
    private static GFontData mediumFontData;
    private static GFontData largeFontData;
    private static GFontData xlargeFontData;
    private static GFontData xxlargeFontData;
    private static GFontData xxxlargeFontData;
    
    public static GFontData getGFontData(int style, boolean largeCache, Canvas canvas) {
        GFontData data = null;
        int size = style & UniFont.SIZE_MASK;
        try {
            if (size == UniFont.SMALL) {
                if (smallFontData == null) {
                    smallFontData = new GFontData(style, largeCache, canvas);
                }
                data = smallFontData;
            } else if (size == UniFont.MEDIUM) {
                if (mediumFontData == null) {
                    mediumFontData = new GFontData(style, largeCache, canvas);
                }
                data = mediumFontData;
            } else if (size == UniFont.LARGE) {
                if (largeFontData == null) {
                    largeFontData = new GFontData(style, largeCache, canvas);
                }
                data = largeFontData;
            } else if (size == UniFont.XLARGE) {
                if (xlargeFontData == null) {
                    xlargeFontData = new GFontData(style, largeCache, canvas);
                }
                data = xlargeFontData;
            } else if (size == UniFont.XXLARGE) {
                if (xxlargeFontData == null) {
                    xxlargeFontData = new GFontData(style, largeCache, canvas);
                }
                data = xxlargeFontData;
            } else /*if (size == UniFont.XXXLARGE)*/ {
                if (xxxlargeFontData == null) {
                    xxxlargeFontData = new GFontData(style, largeCache, canvas);
                }
                data = xxxlargeFontData;
            }
            if (largeCache && !data.hasLargeCache()) {
                data.setLargeCache(largeCache);
            }
            data.refCount++;
            if (largeCache)
                data.largeCacheRefCount++;
            return data;
        } catch (IOException e) {
            throw new IllegalStateException("Cannot initialize font");
        } catch (OutOfMemoryError e) {
            if (data != null)
                data.close(largeCache);
            throw e;
        }
    }

    public void close(boolean neededLargeCache) {
        refCount--;
        if (neededLargeCache)
            largeCacheRefCount--;

        if (refCount <= 0) {
            charBits = null;
            charSet = null;
            charXOff = null;
            charWidth = null;
            charCache = null;
            charCacheGraphics = null;
            cacheHash = null;
            cacheTime = null;
            if (this == smallFontData) {
                smallFontData = null;
            } else if (this == mediumFontData) {
                mediumFontData = null;
            } else if (this == largeFontData) {
                largeFontData = null;
            } else if (this == xlargeFontData) {
                xlargeFontData = null;
            } else if (this == xxlargeFontData) {
                xxlargeFontData = null;
            } else if (this == xxxlargeFontData) {
                xxxlargeFontData = null;
            }
        } else if (largeCache && largeCacheRefCount <= 0) {
            setLargeCache(false);
        }
    }

    private GFontData(int style, boolean largeCache, Canvas canvas) throws IOException {
        String charBitsResource = null;
        String charXOffStr, charWidthStr;

        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();
        int size = style & UniFont.SIZE_MASK;
        bgr = (style & UniFont.BGR_ORDER) != 0;
        sizeX2 = false;
        
        switch (size) {
            case UniFont.SMALL: default:
                charMaxWidth = GFontBase.smallCharMaxWidth;
                charHeight = GFontBase.smallCharHeight;
                charBitsResource = GFontBase.smallCharBitsResource;
                charSet = GFontBase.smallCharSet;
                charXOffStr = GFontBase.smallCharXOff;
                charWidthStr = GFontBase.smallCharWidth;
                break;
            case UniFont.MEDIUM:
                charMaxWidth = GFontBase.mediumCharMaxWidth;
                charHeight = GFontBase.mediumCharHeight;
                charBitsResource = GFontBase.mediumCharBitsResource;
                charSet = GFontBase.mediumCharSet;
                charXOffStr = GFontBase.mediumCharXOff;
                charWidthStr = GFontBase.mediumCharWidth;
                break;
            case UniFont.LARGE: case UniFont.XXLARGE:
                charMaxWidth = GFontBase.largeCharMaxWidth;
                charHeight = GFontBase.largeCharHeight;
                charBitsResource = GFontBase.largeCharBitsResource;
                charSet = GFontBase.largeCharSet;
                charXOffStr = GFontBase.largeCharXOff;
                charWidthStr = GFontBase.largeCharWidth;
                sizeX2 = size==UniFont.XXLARGE;
                break;
            case UniFont.XLARGE: case UniFont.XXXLARGE:
                charMaxWidth = GFontBase.xlargeCharMaxWidth;
                charHeight = GFontBase.xlargeCharHeight;
                charBitsResource = GFontBase.xlargeCharBitsResource;
                charSet = GFontBase.xlargeCharSet;
                charXOffStr = GFontBase.xlargeCharXOff;
                charWidthStr = GFontBase.xlargeCharWidth;
                sizeX2 = size==UniFont.XXXLARGE;
                break;
        }

        InputStream in = getClass().getResourceAsStream(charBitsResource);
        byte[] bits = new byte[charSet.length()
                * ((charHeight * charMaxWidth * 2 * 2 + 7) / 8)];
        for (int pos = 0; pos < bits.length;)
            pos += in.read(bits, pos, bits.length - pos);
        in.close();
        charXOff = new byte[256];
        charWidth = new byte[256];
        for (int i=0; i<256; i++) {
            int pos = charSet.indexOf(i);
            if (pos<0) {
                pos = charSet.indexOf('?');
            }
            charXOff[i] = (byte)charXOffStr.charAt(pos);
            charWidth[i] = (byte)charWidthStr.charAt(pos);
        }
        if (sizeX2) {
            charMaxWidth *= 2;
            charHeight *= 2;
            for (int i=0; i<256; i++) {
                charXOff[i] *= 2;
                charWidth[i] *= 2;
            }
        }
        charBits = bits;
        setLargeCache(largeCache);
    }

    public boolean hasLargeCache() {
        return largeCache;
    }

    void setLargeCache(boolean largeCache) {
        cacheHash = null;
        cacheTime = null;
        charCache = null;
        charCacheGraphics = null;
        this.largeCache = largeCache;
        cacheWidth = largeCache ? 16 : 10;
        if (cacheWidth * charMaxWidth > canvasWidth) {
            cacheWidth = canvasWidth / charMaxWidth;
        }
        cacheHeight = (largeCache ? 128 : 40) / cacheWidth;
        if (cacheHeight * charHeight > canvasHeight) {
            cacheHeight = canvasHeight / charHeight;
        }
        cacheSize = cacheWidth * cacheHeight;
        bucketSize = 4;
        cacheHash = new short[cacheSize];
        cacheTime = new short[cacheSize];
        time = 0;
        charCache = Image.createImage(charMaxWidth * cacheWidth, charHeight
                * cacheHeight);
        charCacheGraphics = charCache.getGraphics();
    }

    int getIndex(char ch, int fg, int bg) {
        int i, i2, j;
        if (++time < 0) { // Ooops, wraparound after 32768 gets
            for (i = 0; i < cacheSize; i++)
                cacheTime[i] = 0; // As an easy way out, all get same priority
            time = 1;
        }
        // Calculated hashes are assumed distinct and nonzero
        short hash = (short) (((ch * 131 + fg) * 137 + bg) % 65537 /* 129169 */);
        int bucketStart = (hash & 0xffff) % cacheSize;

        // Search bucket for the correct hash
        for (i = i2 = bucketStart; i < bucketStart + bucketSize; i++, i2++) {
            if (i2 >= cacheSize)
                i2 -= cacheSize;
            if (cacheHash[i2] == hash) {
                cacheTime[i2] = time;
                // System.out.println("Cache hit for '"+ch+"': "+i2);
                return i2; // Cache hit
            }
        }

        // Cache miss. Find LRU for this bucket
        j = bucketStart;
        for (i = i2 = bucketStart + 1; i < bucketStart + bucketSize; i++, i2++) {
            if (i2 >= cacheSize)
                i2 -= cacheSize;
            if (cacheTime[i2] < cacheTime[j])
                j = i2;
        }
        // if (cacheTime[j] == 0) {
        // System.out.println("Cache miss for '"+ch+"': "+j);
        // } else {
        // System.out.println("Cache collision for '"+ch+"': "+j);
        // }
        cacheHash[j] = hash;
        cacheTime[j] = time;
        return -j - 1; // Signaling cache miss
    }

    void renderChar(int cacheX, int cacheY, int resIndex, int fg, int bg) {
        int fg_r = (fg>>16) & 0xff; fg_r+=(fg_r>>7); // fg_r*256/255+0.5
        int fg_g = (fg>> 8) & 0xff; fg_g+=(fg_g>>7);
        int fg_b =  fg      & 0xff; fg_b+=(fg_b>>7);
        int bg_r = (bg>>16) & 0xff; bg_r+=(bg_r>>7);
        int bg_g = (bg>> 8) & 0xff; bg_g+=(bg_g>>7);
        int bg_b =  bg      & 0xff; bg_b+=(bg_b>>7);

        Graphics g = charCacheGraphics;
        g.setColor(bg);
        g.fillRect(cacheX, cacheY, charMaxWidth, charHeight);
        if (sizeX2) {
            for (int y2 = 0; y2 < charHeight; y2 += 2) {
                int bitPos1, bitPos2;
                bitPos1 = (resIndex * charHeight + y2) * charMaxWidth - 2;
                bitPos2 = (resIndex * charHeight + y2 + 2) * charMaxWidth - 2;
                for (int x2 = 0; x2 < charMaxWidth; x2++) {
                    int gray1, gray2, gray3, gray4, gray5, gray6;
                    if (x2 > 0) {
                        gray1 = ((charBits[bitPos1 / 8] >> (bitPos1 & 7)) & 3) * 85;
                        if (y2 + 2 < charHeight)
                            gray2 = ((charBits[bitPos2 / 8] >> (bitPos2 & 7)) & 3) * 85;
                        else
                            gray2 = 0;
                    } else {
                        gray1 = gray2 = 0;
                    }
                    bitPos1 += 2;
                    bitPos2 += 2;
                    gray3 = ((charBits[bitPos1 / 8] >> (bitPos1 & 7)) & 3) * 85;
                    if (y2 + 2 < charHeight)
                        gray4 = ((charBits[bitPos2 / 8] >> (bitPos2 & 7)) & 3) * 85;
                    else
                        gray4 = 0;
                    if (x2 + 1 < charMaxWidth) {
                        bitPos1 += 2;
                        bitPos2 += 2;
                        gray5 = ((charBits[bitPos1 / 8] >> (bitPos1 & 7)) & 3) * 85;
                        if (y2 + 2 < charHeight)
                            gray6 = ((charBits[bitPos2 / 8] >> (bitPos2 & 7)) & 3) * 85;
                        else
                            gray6 = 0;
                    } else {
                        gray5 = gray6 = 0;
                    }
                    if (gray1 + gray2 + gray3 + gray4 + gray5 + gray6 != 0) {
                        int red, green, blue, tmp;
                        tmp = gray3;
                        gray3 = gray1 + tmp * 3;
                        gray1 = gray1 * 3 + tmp;
                        gray5 = tmp * 3 + gray5;
                        tmp = gray4;
                        gray4 = gray2 + tmp * 3;
                        gray2 = gray2 * 3 + tmp;
                        gray6 = tmp * 3 + gray6;

                        red = (gray1 * 3 + gray2) / 8 - 128;
                        green = (gray3 * 3 + gray4) / 8 - 128;
                        blue = (gray5 * 3 + gray6) / 8 - 128;
                        if (red < 0)
                            red = 0;
                        if (red > 255)
                            red = 255;
                        if (green < 0)
                            green = 0;
                        if (green > 255)
                            green = 255;
                        if (blue < 0)
                            blue = 0;
                        if (blue > 255)
                            blue = 255;
                        if (bgr) {
                            tmp = blue; blue = red; red = tmp;
                        }
                        g.setColor((red  *fg_r+(255-red  )*bg_r)>>8,
                                   (green*fg_g+(255-green)*bg_g)>>8,
                                   (blue *fg_b+(255-blue )*bg_b)>>8);
                        g.fillRect(cacheX + x2, cacheY + y2, 1, 1);

                        red = (gray1 + gray2 * 3) / 8 - 128;
                        green = (gray3 + gray4 * 3) / 8 - 128;
                        blue = (gray5 + gray6 * 3) / 8 - 128;
                        if (red < 0)
                            red = 0;
                        if (red > 255)
                            red = 255;
                        if (green < 0)
                            green = 0;
                        if (green > 255)
                            green = 255;
                        if (blue < 0)
                            blue = 0;
                        if (blue > 255)
                            blue = 255;
                        if (bgr) {
                            tmp = blue; blue = red; red = tmp;
                        }
                        g.setColor((red  *fg_r+(255-red  )*bg_r)>>8,
                                   (green*fg_g+(255-green)*bg_g)>>8,
                                   (blue *fg_b+(255-blue )*bg_b)>>8);
                        g.fillRect(cacheX + x2, cacheY + y2 + 1, 1, 1);
                    }
                    bitPos1 -= 2;
                    bitPos2 -= 2;
                }
            }
        } else {
            for (int y2 = 0; y2 < charHeight; y2++) {
                for (int x2 = 0; x2 < charMaxWidth; x2++) {
                    int red, green, blue, bitPos;
                    bitPos = ((resIndex * charHeight + y2) * charMaxWidth + x2) * 4 - 2;
                    if (x2 > 0)
                        red = ((charBits[bitPos / 8] >> (bitPos & 7)) & 3) * 85;
                    else
                        red = 0;
                    bitPos += 2;
                    green = ((charBits[bitPos / 8] >> (bitPos & 7)) & 3) * 85;
                    bitPos += 2;
                    blue = ((charBits[bitPos / 8] >> (bitPos & 7)) & 3) * 85;
                    if (red + green + blue != 0) {
                        if (bgr) {
                            int tmp = blue; blue = red; red = tmp;
                        }
                        g.setColor((red  *fg_r+(255-red  )*bg_r)>>8,
                                   (green*fg_g+(255-green)*bg_g)>>8,
                                   (blue *fg_b+(255-blue )*bg_b)>>8);
                        g.fillRect(cacheX + x2, cacheY + y2, 1, 1);
                    }
                }
            }
        }
    }

    private int clipX1, clipX2, clipY1, clipY2;

    void getClip(Graphics g) {
        clipX1 = g.getClipX();
        clipY1 = g.getClipY();
        clipX2 = clipX1 + g.getClipWidth();
        clipY2 = clipY1 + g.getClipHeight();
    }
    
    void restoreClip(Graphics g) {
        g.setClip(clipX1, clipY1, clipX2 - clipX1, clipY2 - clipY1);
    }

    int drawGFontChar(Graphics g, int x, int y, char ch, int fg, int bg, boolean monospaced) {
        int index = getIndex(ch, fg, bg);
        boolean cacheMiss = false;
        if (index < 0) {
            index = -index - 1;
            cacheMiss = true;
        }
        int cacheX = charMaxWidth * (index % cacheWidth);
        int cacheY = charHeight * (index / cacheWidth);
        if (cacheMiss) {
            int resIndex = charSet.indexOf(ch);
            if (resIndex < 0)
                resIndex = charSet.indexOf('?');
            renderChar(cacheX, cacheY, resIndex, fg, bg);
        }
        int xOff = 0;
        int width = charMaxWidth;
        if (!monospaced) {
            xOff = charXOff[ch];
            width = charWidth[ch];
        }
        int cx1 = clipX1 > x ? clipX1 : x;
        int cx2 = clipX2 < x + width ? clipX2 : x + width;
        int cy1 = clipY1 > y ? clipY1 : y;
        int cy2 = clipY2 < y + charHeight ? clipY2 : y + charHeight;
        if (cx1 < cx2 && cy1 < cy2) {
            g.setClip(cx1, cy1, cx2 - cx1, cy2 - cy1);
            g.drawImage(charCache, x - cacheX - xOff, y - cacheY,
                    Graphics.TOP | Graphics.LEFT);
        }
        return width;
    }

}