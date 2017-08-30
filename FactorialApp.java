import javafx.application.Application;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FactorialApp extends Application {   

   protected String runFactorial(int upper) {
      // Start threads
      Factorial value = new Factorial();
      int middle = upper/2;
      Thread thrd1 = new Thread(new FactorialRunner(1,middle,value));
      Thread thrd2 = new Thread(new FactorialRunner(middle+1,upper,value));
      thrd1.start();
      thrd2.start();
      // Join threads and output
      double output = -1;
      try {
         // Get the value
         thrd1.join();
         thrd2.join();
         output = value.getValue();
         if (Double.isInfinite(output))
            return upper + "! is too large to output";
         else
            return upper + "! = " + output;
       } catch (Exception e) {
         return "Some other issue...";
       }
   }
   
   @Override
   public void start(Stage primaryStage) throws Exception {
      final TextField numField = new TextField();
      numField.setMaxHeight(TextField.USE_PREF_SIZE);
      numField.setMaxWidth(TextField.USE_PREF_SIZE);
      
      Label label = new Label();
      label.setWrapText(true);
      label.textProperty().bind(new StringBinding() {
         {
            bind(numField.textProperty());
         }
         
         @Override
         protected String computeValue() {
            try {
               return runFactorial(Integer.parseInt(numField.getText()));
            } catch (Exception e) {
               return "Enter a number for factorial (i.e. n! = ?)";
            }
         }
      });
       
      VBox vBox = new VBox(7);
      vBox.setPadding(new Insets(12));
      vBox.getChildren().addAll(label, numField);
      vBox.setAlignment(Pos.CENTER);   
       
      primaryStage.setScene(new Scene(vBox));
      primaryStage.show();
   }
   
   public static void main(String args[]){ launch(args); }
}
/************ Runner for factorial *********************/
class FactorialRunner implements Runnable {
   private int upper;
   private int lower;
   private Factorial value;
   
   public FactorialRunner(int l, int u, Factorial v) {
      upper = u;
      lower = l;
      value = v;
   }
   
   public void run() {
      double product = 1;
      for (int i=lower; i<=upper; i++)
         product *= i;
      value.multiplyBy(product);
   }
}
/************ Class for factorial *********************/
class Factorial {
   private double value;
   public Factorial() { value = 1; }
   public double getValue() { return value; }
   public void multiplyBy(double v) { value *= v; }
}