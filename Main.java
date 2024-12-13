import java.util.Arrays;
import java.util.Scanner;
import java.util.zip.*;
import java.io.IOException;
//Grupo 6
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter image data: ");

        String input = scanner.nextLine();
        String[] arguments = input.split(" ");

        if (arguments.length < 6) {
            System.out.println("Insufficient arguments!!");
            scanner.close();
            return;
        }
        int[][][] image;
        int[][][] quantified_image;
        int[][][] predictedImage;
        int[][][] predictedImageINV;
        float entropy = 0;
        float PSNR = 0;
        float MSE = 0;
        int PAE;
        int maxLevels = 2;
        String path_file = arguments[0];
        int rows = Integer.parseInt(arguments[1]);
        int columns = Integer.parseInt(arguments[2]);
        int components = Integer.parseInt(arguments[3]);
        int bytes_sample = Integer.parseInt(arguments[4]);
        boolean signed = Boolean.parseBoolean(arguments[5]);
        path_file = "../imatges/imatges/n1_GRAY.1_2560_2048_1_0_8_0_0_0.raw";
        //Enter image data: ../imatges/n1_GRAY.1_2560_2048_1_0_8_0_0_0 -> 2560 2048 1 1 0
        Functions functions = new Functions();
        try {
            image = functions.LoadImage(path_file, rows, columns, components, bytes_sample, signed);
            // quantified_image = functions.Quantization(image, 5, 0);
            // quantified_image = functions.Quantization(quantified_image, 5, 1);
            // entropy = functions.Entropy(quantified_image);
            // MSE = functions.MSE(image, quantified_image);
            // PSNR = functions.PSNR(image, quantified_image, MSE);
            // PAE = functions.PAE(image, quantified_image);
            System.out.println("FWD FILAS");
            int level = 0;
            int currentRows = image[0].length;   
            int currentCols = image[0][0].length;

            while(level < maxLevels) {
                for (int i = 0; i < image.length; i++) { 
                    for (int j = 0; j < currentRows; j++) { 
                        int[] fila = image[i][j];
                        functions.RHAAR_fwd(fila, fila, 1);
                    }
                }
                for (int i = 0; i < image.length; i++) { 
                    for (int j = 0; j < currentCols; j++) { 
                        int[] columna = new int[currentRows]; 
                        for (int k = 0; k < currentRows; k++) { 
                            columna[k] = image[i][k][j]; // REVISAR ESTO
                        }
                        functions.RHAAR_fwd(columna, columna, 1);
                        for (int k = 0; k < currentRows; k++) {
                            image[i][k][j] = columna[k];
                        }
                    }
                }

                currentRows /= 2;
                currentCols /= 2;
                level++;
            }
            
            functions.SaveFile(image, 1, false, "../imatges/WaveletFWD.raw");

            System.out.println("INV FILAS");
            for (int i = 0; i < image.length; i++) { // Iterar sobre componentes
                for (int j = 0; j < image[i].length; j++) { // Iterar sobre filas
                    int[] fila = image[i][j];
                    functions.RHAAR_inv(fila, fila, maxLevels);
                }
            }
            System.out.println("INV COLUMNAS");
            for (int i = 0; i < image.length; i++) { // Iterar sobre componentes/caras
                for (int j = 0; j < image[i][0].length; j++) { // Iterar sobre columnas
                    int[] columna = new int[image[i].length]; // Crear un array para almacenar la columna
                    for (int k = 0; k < image[i].length; k++) { // Iterar sobre filas para construir la columna
                        columna[k] = image[i][k][j];
                    }
                    functions.RHAAR_inv(columna, columna, maxLevels);
                }
            }
            functions.SaveFile(image, 1, false, "../imatges/WaveletINV.raw");
            // predictedImage = functions.imagePredictor(image);
            // functions.SaveFile(predictedImage, 1, false, "../imatges/PredictedImage.raw");
            // predictedImageINV = functions.imagePredictorInv(predictedImage);
            // functions.SaveFile(predictedImageINV, 1, false, "../imatges/PredictedImageINV.raw");
            //functions.ZipImage("../imatges/PredictedImage.raw", "../imatges/PredictedImage.zip");
            //functions.UnzipImage("../imatges/PredictedImage.zip", "../imatges/PredictedImageDir");


        }catch (IOException e) {
            System.out.println("An error occurred while loading the image: " + e.getMessage());
            e.printStackTrace(); 
        }
        scanner.close();

    }
}
