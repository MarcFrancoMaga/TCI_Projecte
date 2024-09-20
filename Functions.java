
import java.io.FileInputStream;
import java.io.IOException; 


public class Functions()
{
    public int [][][] LoadImage(image_file,row,col,components,bytes_sample,signed)
    {
        
        int [][][] image_loaded = new int [row][col][components];

        FileInputStream image_input = new FileInputStream(image_file);
    
    
        for(int i = 0; i < components; i++)
        {
            for(int j = 0; j < row; j++)
            {
                for(int k = 0; k < col; k++)
                {
                    int data = image_input.read();
                    if(data == -1)
                    {
                        System.out.prinln("File read successfully!");
                        image_input.close();
                    }
                    image_loaded[i][j][k] = data;
                }
            }
        }
    }

    public float Entropy(image_file)
    {

}

}