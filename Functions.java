import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Functions {

    public int[][][] LoadImage(String image_file, int row, int col, int components, int bytes_sample, boolean signed) throws IOException {
        int[][][] image_loaded = new int[components][row][col];
        FileInputStream image_input = new FileInputStream(image_file);
        System.out.println("Reading File..."); 
        //component -> row -> column
        for (int i = 0; i < components; i++) {
            for (int j = 0; j < row; j++) {
                for (int k = 0; k < col; k++) {
                    if(bytes_sample == 1){
                        int data = image_input.read();
                        image_loaded[i][j][k] = data; 
                    }
                    if(bytes_sample == 2){
                        int byte1 = image_input.read();
                        int byte2 = image_input.read();
                        if (byte1 == -1 || byte2 == -1) {
                            throw new IOException("Unexpected end of file");
                        }
                        int data = (byte1 << 8) | (byte2 & 0xFF);
                        image_loaded[i][j][k] = data; 

                    }
                }
            }
        }
        System.out.println("File read successfully!"); 

        image_input.close(); 
        return image_loaded;
    }

    public void SaveFile(int image_file[][][], int bytes_sample, boolean signed, String path) {
        try(FileOutputStream image_output = new FileOutputStream(path)) {
            System.out.println("Saving File...");
            for (int i = 0; i < image_file.length; i++) {
                for (int j = 0; j < image_file[i].length; j++) {
                    for (int k = 0; k < image_file[i][j].length; k++) {
                        int value = image_file[i][j][k];
                        if (bytes_sample == 1) {
                            image_output.write(value & 0xFF);
                        } else if (bytes_sample == 2) {
                            byte byte1 = (byte) ((value >> 8) & 0xFF); 
                            byte byte2 = (byte) (value & 0xFF);  
                            image_output.write(byte1);
                            image_output.write(byte2);
                        }
                    }
                }
            }
            System.out.println("File saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }

    public float Entropy(int image_file[][][]) {
        int total = 0;
        float entropy = 0;
        int[] frequencies = new int[256]; 
        System.out.println("Calculating entropy...");
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
        int[][][] quantized_image = new int[image_file.length][image_file[0].length][image_file[0][0].length];
        for (int i = 0; i < image_file.length; i++) {
            for (int j = 0; j < image_file[i].length; j++) {
                for (int k = 0; k < image_file[i][j].length; k++) {
                    quantized_image[i][j][k] = image_file[i][j][k];
                }
            }
        }
        System.out.println("Quantizing...");
        for (int i = 0; i < quantized_image.length; i++) {
            for (int j = 0; j < quantized_image[i].length; j++) {
                for (int k = 0; k < quantized_image[i][j].length; k++) {
                    if (q_step >= 1) {
                        if (direction == 0) { // Quantizar
                            if (quantized_image[i][j][k] < 0) {
                                quantized_image[i][j][k] = (int) Math.floor(Math.abs(quantized_image[i][j][k]) / q_step);
                                quantized_image[i][j][k] *= -1;
                            } else {
                                quantized_image[i][j][k] = (int) Math.floor(Math.abs(quantized_image[i][j][k]) / q_step);
                            }
                        } else if (direction == 1) { // Desquantizar
                            quantized_image[i][j][k] *= q_step;
                        }
                    } else {
                        System.out.println("q_step less than 1");
                    }
                }
            }
        }
        return quantized_image;
    }

    public float MSE(int[][][] original_image, int[][][] q_image) {
        int total = 0;
        int count = 0;
        int aux = 0;
        System.out.println("Calculating MSE...");
        for (int i = 0; i < original_image.length; i++) {
            for (int j = 0; j < original_image[i].length; j++) {
                for (int k = 0; k < original_image[i][j].length; k++) {
                    aux = original_image[i][j][k] - q_image[i][j][k]; 
                    total += aux * aux;
                    count++;
                }
            }
        }
        return (float) total / count;
    }

    public float PSNR(int[][][] original_image, int[][][] q_image, float mse) {
        double result = 0;
        System.out.println("Calculating PSNR...");
        result = 10 * Math.log10(Math.pow((Math.pow(2, 8) - 1),2) / mse);
        return (float) result;
    }

    public int PAE(int[][][] original_image, int[][][] q_image) {
        int maxValue = 0;
        int abs_diff = 0;
        System.out.println("Calculating PAE...");
        for (int i = 0; i < original_image.length; i++) {
            for (int j = 0; j < original_image[i].length; j++) {
                for (int k = 0; k < original_image[i][j].length; k++) {
                    abs_diff = original_image[i][j][k] - q_image[i][j][k];
                    if (abs_diff < 0) {
                        abs_diff = -abs_diff; 
                    }
                    
                    if (abs_diff > maxValue) {
                        maxValue = abs_diff; 
                    }                }
            }
        }
        return maxValue;
    }

    public void RHAAR_fwd(int[] input_vector, int[] output_vector) {
        haarTransformRecursiveFWD(input_vector, output_vector, input_vector.length);
    }
    
    private void haarTransformRecursiveFWD(int[] input, int[] output, int length) {
        if (length % 2 != 0) {
            System.out.println("odd vector");
            return;
        }
        
        int[] aux = new int[length];
        for (int i = 0; i < length / 2; i++) {
            aux[i] = (input[2 * i] + input[2 * i + 1]) / 2; //update
            aux[length / 2 + i] = Math.abs(input[2 * i] - input[2 * i + 1]); //prediction
        }
        for (int i = 0; i < aux.length; i++) {
            output[i] = aux[i];
        }
        if (length > 2) {
            haarTransformRecursiveFWD(output, output, length / 2);
        }
    }
    
    public void RHAAR_inv(int[] input_vector, int[] output_vector){
        haarTransformRecursiveINV(input_vector, output_vector, input_vector.length, 2);
    }
    private void haarTransformRecursiveINV(int[] input, int[] output, int length, int minValue){
        if (minValue <= 1){
            output[0] = input[0];
            return;
        }
        System.out.println("\nInput (at minValue = " + minValue + "): ");
        for (int i = 0; i < input.length; i++) {
            System.out.print(input[i] + " ");
        }
        int[] aux = new int[length]; //el problema es que no se guardan en orden; Mirar la consola
        for (int i = 0; i < minValue / 2; i++) {
            int w = input[minValue / 2 + i];
            aux[i] = input[i] - (w / 2);
            aux[minValue / 2 + i] = w + aux[i];
            System.out.println("\ni: " + i);
            System.out.println("w: " + w);
            System.out.println("aux[" + i + "]: " + aux[i]);
            System.out.println("aux[" + (minValue / 2 + i) + "]: " + aux[minValue / 2 + i]);
        }
        System.out.println("\noutput before copy aux: ");
        for (int i = 0; i < output.length; i++) {
            System.out.print(output[i] + " ");
        }
        System.arraycopy(aux, 0, output, 0, minValue);
        System.out.println("\nAux: ");
        for (int i = 0; i < aux.length; i++) {
            System.out.print(aux[i] + " ");
        }System.out.println("\noutput after aux: ");
        for (int i = 0; i < output.length; i++) {
            System.out.print(output[i] + " ");
        }
        if(minValue*2 <= length){
            haarTransformRecursiveINV(output, input, length, minValue * 2);
        }
    }
}   