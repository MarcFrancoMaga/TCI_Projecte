import java.util.Arrays;
import java.util.Scanner;
import java.util.zip.*;
import java.io.IOException;
//Grupo 6
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce los datos de la imagen: ");

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
        int maxLevels = 5;
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
            // System.out.print("Quiere cuantizar?: 1.Si 2.No ");
            // int choice = scanner.nextInt();
            // if(choice == 1){
            //     System.out.print("Cuantos steps quiere hacer?");
            //     int step = scanner.nextInt();
            //     quantified_image = functions.Quantization(image, step, 0);
            //     //quantified_image = functions.Quantization(quantified_image, step, 1);
            // }
            // System.out.print("Quiere WAVELET o PREDECIR?: 1.WAVELET 2.PREDECIR ");
            // choice = scanner.nextInt();
            // switch (choice) {
            //     case 1:
            //         System.out.print("Cuantos niveles de wavelet quiere hacer?");
            //         int wavelet_step = scanner.nextInt();
            //         break;
            //     case 2:
            //         break;
            //     default:
            //         System.out.print("Valor incorrecto");
            //         scanner.close();
            //         break;
            // }
            // entropy = functions.Entropy(quantified_image);
            // MSE = functions.MSE(image, quantified_image);
            // PSNR = functions.PSNR(image, quantified_image, MSE);
            // PAE = functions.PAE(image, quantified_image);
            System.out.println("WAVELET FWD");
            int currentRows = image[0].length;
            int currentCols = image[0][0].length;
            int count = 2;
            System.out.println("entropy pre " + functions.Entropy(image));
            for (int level = 1; level <= maxLevels; level++) {
                for (int i = 0; i < image.length; i++) {
                    // Transformada en filas
                    for (int j = 0; j < currentRows; j++) {
                        int[] fila = new int[currentCols];
                        for (int k = 0; k < currentCols; k++) {
                            fila[k] = image[i][j][k];
                        }
                        functions.RHAAR_fwd(fila, fila, 1);
                        for (int k = 0; k < currentCols; k++) {
                            image[i][j][k] = fila[k];
                        }
                    }
                    // Transformada en columnas
                    for (int j = 0; j < currentCols; j++) {
                        int[] columna = new int[currentRows];
                        for (int k = 0; k < currentRows; k++) {
                            columna[k] = image[i][k][j];
                        }
                        functions.RHAAR_fwd(columna, columna, 1);
                        for (int k = 0; k < currentRows; k++) {
                            image[i][k][j] = columna[k];
                        }
                    }
                }
                if(count <= maxLevels){
                    currentRows /= 2;
                    currentCols /= 2;
                }
                count++;
            }
            functions.SaveFile(image, 1, false, "../imatges/WaveletFWD.raw");
            count = 2;
            System.out.println("WAVELET INV");
            for (int level = maxLevels; level >= 1; level--) {                           
                for (int i = 0; i < image.length; i++) {
                    for (int j = 0; j < currentCols; j++) {
                        int[] columna = new int[currentRows];
                        for (int k = 0; k < currentRows; k++) {
                            columna[k] = image[i][k][j];
                        }
                        functions.RHAAR_inv(columna, columna, 1);
                        for (int k = 0; k < currentRows; k++) {
                            image[i][k][j] = columna[k];
                        }
                    }
                    for (int j = 0; j < currentRows; j++) {
                        int[] fila = new int[currentCols];
                        for (int k = 0; k < currentCols; k++) {
                            fila[k] = image[i][j][k];
                        }
                        functions.RHAAR_inv(fila, fila, 1);
                        for (int k = 0; k < currentCols; k++) {
                            image[i][j][k] = fila[k];
                        }
                    }
                    if(count <= maxLevels){
                        currentRows *= 2;
                        currentCols *= 2;
                    }
                    count++;
                }
            }            
        
            System.out.println("entropy post " + functions.Entropy(image));
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
