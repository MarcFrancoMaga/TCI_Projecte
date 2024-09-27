import java.io.FileInputStream;
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
            int total = 0; // FILAS X COLUMNAS
            float entropy = 0;
            int[] frequencies = new int[256];
            
        for (int i = 0; i < image_file.length; i++) {
            for (int j = 0; j < image_file[i].length; j++) {
                for (int k = 0; k < image_file[i][j].length; k++) {
                    int value = image_file[i][j][k];
                    if (value >= 0 && value <= 255) {
                        frequencies[value]++;
                    }
                }
            }
            //DIVIDIR CADA CASILLA DEL ARRAY ENTRE EL TOTAL 
            //LUEGO HACER LA FORMULA DE LA ENTROPIA CON CADA CASILLA
        for (int i = 0; i < image_file.length; i++) {
            for (int j = 0; j < image_file[i].length; j++) {
                for (int k = 0; k < image_file[i][j].length; k++) {
                    if(image_file_new[i][j][k] == 0){
                        entropy += 0;
                    }
                    else{
                        entropy += (-image_file_new[i][j][k]) * (Math.log(image_file_new[i][j][k]) / Math.log(2));
                    }
                }
            }
        }
        return entropy;

    }
}