import java.util.Arrays;
import java.util.Scanner;
import java.util.zip.*;
import java.io.IOException;
import java.io.File;

//Grupo 6
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //Enter image data: ../imatges/n1_GRAY.1_2560_2048_1_0_8_0_0_0 -> 2560 2048 1 1 0
        Functions functions = new Functions();
        try {
            System.out.println("¿Qué deseas hacer?\n1. Comprimir\n2. Descomprimir");
            int opcion_codificar = scanner.nextInt();
            scanner.nextLine(); 
            switch(opcion_codificar)
            {
                case 1:
                    System.out.print("Introduce los datos de la imagen: ");
                    String input = scanner.nextLine();
                    String[] arguments = input.split(" ");

                    if (arguments.length < 6) {
                        System.out.println("Insufficient arguments!!");
                        scanner.close();
                        return;
                    }
                    int[][][] image;
                    String path_file = arguments[0];
                    int rows = Integer.parseInt(arguments[1]);
                    int columns = Integer.parseInt(arguments[2]);
                    int components = Integer.parseInt(arguments[3]);
                    int bytes_sample = Integer.parseInt(arguments[4]);
                    boolean signed = Boolean.parseBoolean(arguments[5]);
                    path_file = "../imatges/imatges/n1_GRAY.1_2560_2048_1_0_8_0_0_0.raw";
                    image = functions.LoadImage(path_file, rows, columns, components, bytes_sample, signed);
                    int[][][] quantified_image = new int[image.length][image[0].length][image[0][0].length];
                    int[][][] predictedImage = new int[image.length][image[0].length][image[0][0].length];
                    System.out.print("¿Deseas cuantizar los datos?\n1. Sí\n2. No: ");
                    int quantizationChoice = scanner.nextInt();
                    scanner.nextLine();
                    if(quantizationChoice == 1){
                        System.out.print("Cuantos steps quiere hacer? ");
                        int step = scanner.nextInt();
                        scanner.nextLine();
                        quantified_image = functions.Quantization(image, step, 0);
                        for (int i = 0; i < quantified_image.length; i++) {
                            for (int j = 0; j < quantified_image[i].length; j++) {
                                for (int k = 0; k < quantified_image[i][j].length; k++) {
                                    image[i][j][k] = quantified_image[i][j][k];
                                }
                            }
                        }
                    }
                    System.out.print("¿Qué método deseas usar?\n1. Wavelet\n2. Predictor: ");
                    int methodChoice = scanner.nextInt();
                    scanner.nextLine();
                    switch (methodChoice) {
                        case 1:
                            System.out.print("Cuantos niveles de wavelet quiere hacer? ");
                            int maxLevels = scanner.nextInt();
                            scanner.nextLine();
                            int count = 2;
                            int currentRows = image[0].length;
                            int currentCols = image[0][0].length;
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
                            System.out.println("entropia " + functions.Entropy(image));
                            break;
                        case 2:
                            predictedImage = functions.imagePredictor(image);
                            scanner.nextLine();
                            for (int i = 0; i < predictedImage.length; i++) {
                                for (int j = 0; j < predictedImage[i].length; j++) {
                                    for (int k = 0; k < predictedImage[i][j].length; k++) {
                                        image[i][j][k] = predictedImage[i][j][k];
                                    }
                                }
                            }
                            break;
                        default:
                            System.out.print("Valor incorrecto");
                            scanner.close();
                            break;
                    }
                    System.out.print("Indica el path dónde quiere guardar la imagen comprimida ");
                    String zipSavedPath = scanner.nextLine().trim();
                    if (!zipSavedPath.toLowerCase().endsWith(".zip")) {
                        zipSavedPath += ".zip";
                    }
                    System.out.print("Comprimiendo imagen...");
                    functions.SaveFile(image, 1, false, "../imatges/CompressedImage.raw");
                    functions.ZipImage("../imatges/CompressedImage.raw", zipSavedPath);
                    File originalFile = new File("../imatges/CompressedImage.raw");
                    if (originalFile.exists()) {
                        originalFile.delete();
                    } else {
                        System.out.println("La imagen original no existe.");
                    }
                    System.out.println("Imagen comprimida guardada exitosamente en: " + zipSavedPath);
                    break;
                case 2:
                    System.out.println("Especifique la ruta del .ZIP: ");
                    zipSavedPath = scanner.nextLine().trim();
                    if (!zipSavedPath.toLowerCase().endsWith(".zip")) {
                        zipSavedPath += ".zip";
                    }
                    System.out.println("Especifique la ruta donde guardar la imagen: ");
                    String destPathImage = scanner.nextLine();
                    String extractedFileName = functions.UnzipImage(zipSavedPath, destPathImage);
                    System.out.print("Introduce los datos de la imagen: ");
                    input = scanner.nextLine();
                    arguments = input.split(" ");

                    if (arguments.length < 6) {
                        System.out.println("Insufficient arguments!!");
                        scanner.close();
                        return;
                    }
                    path_file = arguments[0];
                    rows = Integer.parseInt(arguments[1]);
                    columns = Integer.parseInt(arguments[2]);
                    components = Integer.parseInt(arguments[3]);
                    bytes_sample = Integer.parseInt(arguments[4]);
                    signed = Boolean.parseBoolean(arguments[5]);
                    path_file = destPathImage + File.separator + extractedFileName;
                    System.out.println(path_file);
                    image = functions.LoadImage(path_file, rows, columns, components, bytes_sample, signed);
                    quantified_image = new int[image.length][image[0].length][image[0][0].length];
                    predictedImage = new int[image.length][image[0].length][image[0][0].length];

                    System.out.print("¿Qué método deseas usar?\n1. Wavelet\n2. Predictor: ");
                    methodChoice = scanner.nextInt();
                    scanner.nextLine();
                    switch (methodChoice) {
                        case 1:
                            System.out.print("Cuantos niveles de wavelet quiere deshacer?");
                            int maxLevels = scanner.nextInt();
                            scanner.nextLine();                            
                            int count = 2;
                            int currentRows = image[0].length;
                            int currentCols = image[0][0].length;
                            for (int i = 1; i < maxLevels; i++){
                                currentRows /= 2;
                                currentCols /=2;
                            }
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
                            break;
                        case 2:
                            predictedImage = functions.imagePredictorInv(image);
                            scanner.nextLine();
                            for (int i = 0; i < predictedImage.length; i++) {
                                for (int j = 0; j < predictedImage[i].length; j++) {
                                    for (int k = 0; k < predictedImage[i][j].length; k++) {
                                        image[i][j][k] = predictedImage[i][j][k];
                                    }
                                }
                            }
                            break;
                        default:
                            System.out.print("Valor incorrecto");
                            scanner.close();
                            break;
                    }
                    System.out.print("¿Deseas descuantizar los datos?\n1. Sí\n2. No: ");
                    int option_descuantizacion = scanner.nextInt();
                    scanner.nextLine();
                    if(option_descuantizacion == 1){
                        System.out.print("Cuantos steps quiere hacer? ");
                        int step = scanner.nextInt();
                        scanner.nextLine();
                        quantified_image = functions.Quantization(image, step, 1);
                        for (int i = 0; i < quantified_image.length; i++) {
                            for (int j = 0; j < quantified_image[i].length; j++) {
                                for (int k = 0; k < quantified_image[i][j].length; k++) {
                                    image[i][j][k] = quantified_image[i][j][k];
                                }
                            }
                        }
                    }
                    System.out.println("Guardando imagen...");
                    functions.SaveFile(image,bytes_sample, signed,"../imatges/ImagenOriginal.raw");
                    break;
                default:
                System.out.println("Valor incorrecto");
                    break;
            }  
        }catch (IOException e) {
            System.out.println("An error occurred while loading the image: " + e.getMessage());
            e.printStackTrace();
        }
        scanner.close();

    }
}
