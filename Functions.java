import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Functions {

    public int[][][] LoadImage(String image_file, int row, int col, int components, int bytes_sample, boolean signed) throws IOException {
        int[][][] image_loaded = new int[components][row][col];
        FileInputStream image_input = new FileInputStream(image_file);
        System.out.println("Reading File"); 
        //component -> row -> column
        for (int i = 0; i < components; i++) {
            for (int j = 0; j < row; j++) {
                for (int k = 0; k < col; k++) {
                    int data = image_input.read();
                    image_loaded[i][j][k] = data; 
                }
            }
        }
        System.out.println("File read successfully!"); 

        image_input.close(); 
        return image_loaded;
    }

    public float Entropy(int image_file[][][]) {
        int total = 0;
        float entropy = 0;
        int[] frequencies = new int[256]; 
        System.out.println("Calculating entropy");
        for (int i = 0; i < image_file.length; i++) {
            for (int j = 0; j < image_file[i].length; j++) {
                for (int k = 0; k < image_file[i][j].length; k++) {
                    int value = image_file[i][j][k];
                    if (value >= 0 && value <= 255) {
                        frequencies[value]++;
                        total++; 
                    }
                }
            }
        }
    
        float[] probabilities = new float[256];
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                probabilities[i] = (float) frequencies[i] / total; 
            }
        }
    
        for (int i = 0; i < probabilities.length; i++) {
            if (probabilities[i] > 0) {
                entropy += -probabilities[i] * (Math.log(probabilities[i]) / Math.log(2));
            }
        }
    
        return entropy;
    }
    
    public int[][][] Quantization(int image_file[][][], int q_step, int direction) {
        for (int i = 0; i < image_file.length; i++) {
            for (int j = 0; j < image_file[i].length; j++) {
                for (int k = 0; k < image_file[i][j].length; k++) {
                    if (q_step >= 1) {
                        if (direction == 0) {
                            if (image_file[i][j][k] < 0){
                                image_file[i][j][k] = (int) Math.floor(Math.abs(image_file[i][j][k]) / q_step);
                                image_file[i][j][k] *= -1; 
                            }
                            else {
                                image_file[i][j][k] = (int) Math.floor(Math.abs(image_file[i][j][k]) / q_step);
                            }
                        }
                        else {
                            image_file[i][j][k] =  image_file[i][j][k] * q_step;
                        }
                       
                    } else {
                        System.out.println("q_step less than 1");
                    }
                    
                }
            }
        }
        return image_file;
    }
    
    public void SaveFile(int image_file[][][], int bytes_sample, boolean signed, String path) {
        try(FileOutputStream image_output = new FileOutputStream(path)) {
            System.out.println("Saving File");
            for (int i = 0; i < image_file.length; i++) {
                for (int j = 0; j < image_file[i].length; j++) {
                    for (int k = 0; k < image_file[i][j].length; k++) {
                        int value = image_file[i][j][k];

                        if (bytes_sample == 1) {
                            image_output.write(value);
                        }
                    }
                }
            }
            System.out.println("File saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }

    public float MSE(int[][][] original_image, int[][][] q_image) {
        int total = 0;
        int count = 0;
        System.out.println("Calculating MSE");
        for (int i = 0; i < original_image.length; i++) {
            for (int j = 0; j < original_image[i].length; j++) {
                for (int k = 0; k < original_image[i][j].length; k++) {
                    int aux = original_image[i][j][k] - q_image[i][j][k]; 
                    total += Math.pow(aux, 2);
                    count++;
                }
            }
        }
        return (float) total / count;
    }

    public float PSNR(int[][][] original_image, int[][][] q_image, int mse) {
        double result = 0;
        result = 10 * Math.log10((Math.pow(2, 8) - 1) / mse);
        return (float) result;
    }
}   