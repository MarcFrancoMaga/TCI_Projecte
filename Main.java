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
        float entropy = 0;
        float PSNR = 0;
        float MSE = 0;
        int PAE;
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

            //functions.SaveFile(quantified_image, 1, false, "../imatges/prueba.raw")

            // int maxLevels = Numero maximo de niveles de la wavelet
            int[] input_vector = {1,2,3,4,5,6,7,8};
            int[] output_vector = new int[input_vector.length];
            int[] outputput_vector = new int[input_vector.length];
            functions.RHAAR_fwd(input_vector, output_vector, 1);
            System.out.println("output");
            for(int i = 0; i < output_vector.length; i++){
                System.out.println(output_vector[i]);
            }
            System.arraycopy(output_vector, 0, outputput_vector, 0, output_vector.length);
            functions.RHAAR_inv(output_vector, outputput_vector, 1);
            System.out.println("outputput");

            for(int i = 0; i < outputput_vector.length; i++){
                System.out.println(outputput_vector[i]);
            }

            // for (int i = 0; i < image.length; i++) { // Iterar sobre componentes
            //      for (int j = 0; j < image[i].length; j++) { // Iterar sobre filas
            //          int[] fila = image[i][j];
            //          functions.RHAAR_fwd(fila, fila, 5);
            //      }
            //  }
            // functions.SaveFile(image, 1, false, "../imatges/prueba1.raw");
            //  for (int i = 0; i < image.length; i++) { // Iterar sobre componentes
            //      for (int j = 0; j < image[i].length; j++) { // Iterar sobre filas
            //          int[] fila = image[i][j];
            //          functions.RHAAR_inv(fila, fila,5);
            //      }
            //  }
            predictedImage = functions.imagePredictor(image);
            functions.SaveFile(image, 1, false, "../imatges/prueba2.raw");


        }catch (IOException e) {
            System.out.println("An error occurred while loading the image: " + e.getMessage());
            e.printStackTrace(); 
        }
        scanner.close();

    }
}
