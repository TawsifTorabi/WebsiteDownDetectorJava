//import libraries
import javax.net.ssl.SSLException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.*;


public class Main extends JFrame{
    //URL Input Text
    private final JTextField urlInput = new JTextField("", 20);
    //Status Section Objects
    private final JLabel statusText = new JLabel("Status:");
    private final JLabel statusOut = new JLabel("Standby");


    public boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
        return true;
    }



    //Method to validate the inputs
    //set the initial validation status
    boolean validStatus = true;
    public void validateData(ActionEvent e){
        String URL = urlInput.getText();
        if(URL.isEmpty()){
            validStatus = false;
            statusText.setForeground(Color.RED);
            statusOut.setText("Please Input URL");
        }
        if(isValidURL(URL)){
            validStatus = true;
            try {
                if(TestURL(URL)){
                    statusOut.setForeground(Color.BLUE);
                    statusOut.setText("Website is Online!");
                }
            } catch (SSLException ex) {
                statusOut.setForeground(Color.RED);
                statusOut.setText("Website is Online! Invalid SSL Certificate");
            } catch (UnknownHostException ex) {
                statusOut.setForeground(Color.RED);
                statusOut.setText("This Website does not exist");
            } catch (IOException ex) {
                statusOut.setForeground(Color.RED);
                statusOut.setText("Error Connecting");
            }
        }else{
            validStatus = false;
            statusOut.setForeground(Color.BLACK);
            statusOut.setText("Please Input valid URL");
        }
    }

    //Method to clear the form
    public void actionClear(ActionEvent e){
        statusOut.setForeground(Color.BLACK);
        urlInput.setText("");
        statusOut.setText("Standby");
    }


    //Check If The URL is Available By Creating HTTP Request
    public boolean TestURL(String url) throws IOException {
        boolean result;
        HttpURLConnection connection;
        connection = (HttpURLConnection)(new URL(url)).openConnection();
        statusOut.setForeground(Color.magenta);
        statusOut.setText("Connecting... Wait for a Moment");
        connection.setConnectTimeout(4 * 1000);
        result = (connection.getResponseCode() == 200);
        if (connection.getResponseCode() != 200){
            statusOut.setForeground(Color.RED);
            statusOut.setText("Website is Offline!");
        }
        return result;
    }




    public Main(){
        super("Website Down Detector");
        setSize(500,150);
        setLocation(500,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Get Containers for contents
        Container mainContainer = this.getContentPane();
        //set layout for main container
        mainContainer.setLayout(new GridLayout(2,1));


        //Button Section Objects
        JButton testButton = new JButton("Test");
        JButton clearButton = new JButton("Clear");

        //Create Panel 1
        JPanel panel1 = new JPanel();
        //Set Flow Layout for Panel 1
        panel1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
        //URL  Input Area
        panel1.add(new JLabel("URL :", SwingConstants.CENTER));
        panel1.add(urlInput);
        urlInput.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    testButton.doClick();
                }
            }
        });
        testButton.addActionListener(this::validateData);
        clearButton.addActionListener(this::actionClear);
        panel1.add(testButton);
        panel1.add(clearButton);
        mainContainer.add(panel1);




        //Create Panel 7
        JPanel panel7 = new JPanel();
        //Set Flow Layout for Panel 7
        panel7.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
        //Buttons Area
        statusOut.setForeground(Color.BLACK);
        panel7.add(statusText);
        panel7.add(statusOut);
        mainContainer.add(panel7);

        setVisible(true);
        setResizable(false);
        pack();
    }

    public static void main(String[] args){
        new Main();
    }
}
