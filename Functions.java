import java.io.FileInputStream;
import java.io.IOException;

public class Functions {

    public int[][][] LoadImage(String image_file, int row, int col, int components, int bytes_sample, boolean signed) throws IOException {
        int[][][] image_loaded = new int[row][col][components];

        FileInputStream image_input = new FileInputStream(image_file);

        for (int i = 0; i < components; i++) {
            for (int j = 0; j < row; j++) {
                for (int k = 0; k < col; k++) {
                    int data = image_input.read();
                    if (data == -1) {
                        System.out.println("File read successfully!"); 
                        image_input.close();
                        return image_loaded; 
                    }
                    image_loaded[j][k][i] = data; 
                }
            }
        }
        image_input.close(); 
        return image_loaded;
    }

    public float Entropy(String image_file) {
        return 0.0f;
    }
}
