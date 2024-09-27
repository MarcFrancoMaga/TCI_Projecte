import java.util.Scanner;
import java.io.IOException;

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
        float entropy = 0;
        String path_file = arguments[0];
        int rows = Integer.parseInt(arguments[1]);
        int columns = Integer.parseInt(arguments[2]);
        int components = Integer.parseInt(arguments[3]);
        int bytes_sample = Integer.parseInt(arguments[4]);
        boolean signed = Boolean.parseBoolean(arguments[5]);
        path_file = "../imatges/imatges/n1_GRAY.1_2560_2048_1_0_8_0_0_0.raw";
        /*
        rows = 512;
        columns = 512;
        components = ;
        bytes_sample = ;
        signed = ; */
        Functions functions = new Functions();
        try {
            image = functions.LoadImage(path_file, rows, columns, components, bytes_sample, signed);
            /* 
            for (int i = 0; i < image.length; i++) {
                for (int j = 0; j < image[i].length; j++) {
                    for (int k = 0; k < image[i][j].length; k++) {
                        System.out.print(image[i][j][k] + " ");
                    }
                    System.out.println();
                }
                System.out.println(); 
            }*/
           entropy = functions.Entropy(image);
           System.out.println(entropy);

        }        
        catch (IOException e) {
            System.out.println("An error occurred while loading the image: " + e.getMessage());
            e.printStackTrace(); 
        }
        scanner.close();

    }
}
