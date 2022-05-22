import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;

public class ImageProcessing {

    public static int[][] imgToTwoD(String inputFileOrLink) {
        try {
            BufferedImage image = null;
            if (inputFileOrLink.substring(0, 4).toLowerCase().equals("http")) {
                URL imageUrl = new URL(inputFileOrLink);
                image = ImageIO.read(imageUrl);
                if (image == null) {
                    System.out.println("Failed to get image from provided URL.");
                }
            } else {
                image = ImageIO.read(new File(inputFileOrLink));
            }

            int imgRows = image.getHeight();
            int imgCols = image.getWidth();
            int[][] pixelData = new int[imgRows][imgCols];

            for (int i = 0; i < imgRows; i++) {
                for (int j = 0; j < imgCols; j++) {
                    pixelData[i][j] = image.getRGB(j, i);
                }
            }

            return pixelData;
        } catch (Exception e) {
            System.out.println("Failed to load image: " + e.getLocalizedMessage());
            return null;
        }
    }

    public static void twoDToImage(int[][] imgData, String fileName) {
		try {
			int imgRows = imgData.length;
			int imgCols = imgData[0].length;
			BufferedImage result = new BufferedImage(imgCols, imgRows, BufferedImage.TYPE_INT_RGB);

			for (int i = 0; i < imgRows; i++) {
				for (int j = 0; j < imgCols; j++) {
					result.setRGB(j, i, imgData[i][j]);
				}
			}

			File output = new File(fileName);
			ImageIO.write(result, "jpg", output);
		} catch (Exception e) {
			System.out.println("Failed to save image: " + e.getLocalizedMessage());
		}
	}

	public static int[] getRGBAFromPixel(int pixelColorValue) {
		Color pixelColor = new Color(pixelColorValue);
		return new int[] { pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelColor.getAlpha() };
	}

	public static int getColorIntValFromRGBA(int[] colorData) {
		if (colorData.length == 4) {
			Color color = new Color(colorData[0], colorData[1], colorData[2], colorData[3]);
			return color.getRGB();
		} else {
			System.out.println("Incorrect number of elements in RGBA array.");
			return -1;
		}
	}

	public static void viewImageData(int[][] imageTwoD) {
		if (imageTwoD.length > 3 && imageTwoD[0].length > 3) {
			int[][] rawPixels = new int[3][3];
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					rawPixels[i][j] = imageTwoD[i][j];
				}
			}
			System.out.println("Raw pixel data from the top left corner.");
			System.out.print(Arrays.deepToString(rawPixels).replace("],", "],\n") + "\n");

			int[][][] rgbPixels = new int[3][3][4];
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					rgbPixels[i][j] = getRGBAFromPixel(imageTwoD[i][j]);
				}
			}
			System.out.println();
			System.out.println("Extracted RGBA pixel data from top the left corner.");

			for (int[][] row : rgbPixels) {
				System.out.print(Arrays.deepToString(row) + System.lineSeparator());
			}
		} else {
			System.out.println("The image is not large enough to extract 9 pixels from the top left corner");
		}
	}


    ///My Functions
    public static int[][] trimAllBorders(int[][] imageTwoD, int pixelCount) {
		// Example Method
		if (imageTwoD.length > pixelCount * 2 && imageTwoD[0].length > pixelCount * 2) {
			int[][] trimmedImg = new int[imageTwoD.length - pixelCount * 2][imageTwoD[0].length - pixelCount * 2];
			for (int i = 0; i < trimmedImg.length; i++) {
				for (int j = 0; j < trimmedImg[i].length; j++) {
					trimmedImg[i][j] = imageTwoD[i + pixelCount][j + pixelCount];
				}
			}
			return trimmedImg;
		} else {
			System.out.println("Cannot trim that many pixels from the given image.");
			return imageTwoD;
		}
	}

    public static int[][] trimBordersByWidthHeight(int[][] imageTwoD, int pixelCountWidth, int pixelCountHeight) {

		if (imageTwoD.length > pixelCountHeight * 2 && imageTwoD[0].length > pixelCountWidth * 2) {
			int[][] trimmedImg = new int[imageTwoD.length - pixelCountHeight * 2][imageTwoD[0].length - pixelCountWidth * 2];
			for (int i = 0; i < trimmedImg.length; i++) {
				for (int j = 0; j < trimmedImg[i].length; j++) {
					trimmedImg[i][j] = imageTwoD[i + pixelCountHeight][j + pixelCountWidth];
				}
			}
			return trimmedImg;
		} else {
			System.out.println("Cannot trim that many pixels from the given image.");
			return imageTwoD;
		}
	}

    public static int[][] negativeColor(int[][] imageTwoD) {
		int[][] negativeImg = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                rgba[0] = 255 - rgba[0];
				rgba[1] = 255 - rgba[1];
				rgba[2] = 255 - rgba[2];
                negativeImg[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
		return negativeImg;
	}

    public static int[][] stretchWidth(int[][] imageTwoD) {
		int[][] stretchedWidthImg = new int[imageTwoD.length][imageTwoD[0].length * 2];
		int it = 0;
		for (int i = 0; i < imageTwoD.length; i++) {
			for (int j = 0; j < imageTwoD[i].length; j++) {
				it = j * 2;
				stretchedWidthImg[i][it] = imageTwoD[i][j];
				stretchedWidthImg[i][it + 1] = imageTwoD[i][j];
			}
		}
		return stretchedWidthImg;
	}

    public static int[][] stretchHeight(int[][] imageTwoD) {
		int[][] stretchedHeightImg = new int[imageTwoD.length * 2][imageTwoD[0].length];
		int it = 0;
		for (int i = 0; i < imageTwoD.length; i++) {
			for (int j = 0; j < imageTwoD[i].length; j++) {
				it = i * 2;
				stretchedHeightImg[it][j] = imageTwoD[i][j];
				stretchedHeightImg[it + 1][j] = imageTwoD[i][j];
			}
		}
		return stretchedHeightImg;
	}

    public static int[][] shrinkHeight(int[][] imageTwoD) {
		int[][] shrunkHeightImg = new int[imageTwoD.length/2][imageTwoD[0].length];
		for (int i = 0; i < imageTwoD[0].length; i++) {
			for (int j = 0; j < imageTwoD.length - 1; j += 2) {
				shrunkHeightImg[j / 2][i] = imageTwoD[j][i];
			}
		}
		return shrunkHeightImg;
	}

    public static int[][] shrinkWidth(int[][] imageTwoD) {
        int[][] shrunkWidthImg = new int[imageTwoD.length][imageTwoD[0].length/2];
        for (int i = 0; i < imageTwoD.length; i++) {
			for (int j = 0; j < imageTwoD[i].length - 1; j += 2) {
				shrunkWidthImg[i][j/2] = imageTwoD[i][j];
			}
		}
		return shrunkWidthImg;
    }

    public static int[][] invertImage(int[][] imageTwoD) {
		int[][] invertedImg = new int[imageTwoD.length][imageTwoD[0].length];
		for (int i = 0; i < imageTwoD.length; i++) {
			for (int j = 0; j < imageTwoD[i].length; j++) {
				invertedImg[i][j] = imageTwoD[(imageTwoD.length - 1) - i][(imageTwoD[i].length - 1) - j];
			}
		}
		return invertedImg;
	}

    public static int[][] rotateImage(int[][] imageTwoD) {
        int n = imageTwoD.length;
        int m = imageTwoD[0].length;
        int[][] rotatedImg = new int[m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                rotatedImg[j][n-1-i] = imageTwoD[i][j];
            }
        }
        return rotatedImg;
    }   

    public static int[][] colorFilter(int[][] imageTwoD, int redChangeValue, int greenChangeValue, int blueChangeValue) {
        int[][] colorAdjImg = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                int newRed = rgba[0] + redChangeValue;
				int newGreen = rgba[1] + greenChangeValue;
				int newBlue = rgba[2] + blueChangeValue;

				if (newRed > 255) {
					newRed = 255;
				} else if (newRed < 0) {
					newRed = 0;
				}

				if (newGreen > 255) {
					newGreen = 255;
				} else if (newGreen < 0) {
					newGreen = 0;
				}

				if (newBlue > 255) {
					newBlue = 255;
				} else if (newBlue < 0) {
					newBlue = 0;
				}

				rgba[0] = newRed;
				rgba[1] = newGreen;
				rgba[2] = newBlue;

				colorAdjImg[i][j] = getColorIntValFromRGBA(rgba);
			}
		}
		return colorAdjImg;
	}


    public static int[][] paintRandomImage(int[][] canvas) {
		Random rand = new Random();
		for (int i = 0; i < canvas.length; i++) {
			for (int j = 0; j < canvas[i].length; j++) {
				int[] rgba = { rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 255 };
				canvas[i][j] = getColorIntValFromRGBA(rgba);
			}
		}
		return canvas;
	}
        
	public static int[][] paintRectangle(int[][] canvas, int width, int height, int rowPosition, int colPosition, int color) {
		for (int i = 0; i < canvas.length; i++) {
			for (int j = 0; j < canvas[i].length; j++) {
				if (i >= rowPosition && i <= rowPosition + width) {
					if (j >= colPosition && j <= colPosition + height) {
						canvas[i][j] = color;
					}
				}
			}
		}
		return canvas;
	}

	public static int[][] generateRectangles(int[][] canvas, int numRectangles) {
		Random rand = new Random();
		for (int i = 0; i < numRectangles; i++) {
			int randomWidth = rand.nextInt(canvas[0].length);
			int randomHeight = rand.nextInt(canvas.length);
			int randomRowPos = rand.nextInt(canvas.length - randomHeight);
			int randomColPos = rand.nextInt(canvas[0].length - randomWidth);
			int[] rgba = { rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 255 };
			int randomColor = getColorIntValFromRGBA(rgba);
			canvas = paintRectangle(canvas, randomWidth, randomHeight, randomRowPos, randomColPos, randomColor);
		}
		return canvas;
	}





	public static void main(String[] args) {
        int[][] imageData = imgToTwoD("images/img1.jpg");
      
        int[][] trimmedAll = trimAllBorders(imageData, 500);
	    twoDToImage(trimmedAll, "images/imgTrimmedAll.jpg");

        int[][] trimmedByWidthHeight = trimBordersByWidthHeight(imageData, 25, 100);
	    twoDToImage(trimmedByWidthHeight, "images/imgTrimmedWH.jpg");

        int[][] negative = negativeColor(imageData);
        twoDToImage(negative, "images/imgNegative.jpg");

        int[][] stretchedWImg = stretchWidth(imageData);
		twoDToImage(stretchedWImg, "images/imgDoubledWidth.jpg");

        int[][] stretchedHImg = stretchHeight(imageData);
		twoDToImage(stretchedHImg, "images/imgDoubledHeight.jpg");

        int[][] shrunkHImg = shrinkHeight(imageData);
        twoDToImage(shrunkHImg, "images/imgHalveHeight.jpg");

        int[][] shrunkWImg = shrinkWidth(imageData);
        twoDToImage(shrunkWImg, "images/imgHalveWidth.jpg");

        int[][] invertImg = invertImage(imageData);
        twoDToImage(invertImg, "images/imgInverted.jpg");

        int[][] rotateImg = rotateImage(imageData);
        twoDToImage(rotateImg, "images/imgRotated.jpg");

        int[][] colorImg = colorFilter(imageData, -75, 30, -30);
		twoDToImage(colorImg, "images/imgColorAdj.jpg");

        int[][] blankImg = new int[1000][2000];
		int[][] randomImg = paintRandomImage(blankImg);
		twoDToImage(randomImg, "./random_img.jpg");

		int[] rgba = { 255, 255, 0, 255 };
		int[][] rectangleImg = paintRectangle(randomImg, 200, 200, 100, 100, getColorIntValFromRGBA(rgba));
		twoDToImage(rectangleImg, "./rectangle.jpg");

		int[][] generatedRectangles = generateRectangles(randomImg, 1000);
		twoDToImage(generatedRectangles, "./generated_rect.jpg");

    }
}
