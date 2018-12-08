package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import jssc.*;


public class ComController { /*Класс чтения из порта*/
    private static SerialPort serialPort; /*Создаем объект типа SerialPort*/

    public static void closePort(){
        try {
            if (serialPort!=null && serialPort.isOpened()) {
                serialPort.closePort();
            }
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    @FXML
    TextField inputID;
    @FXML
    TextField outputID;



    public ComController(){
        serialPort=new SerialPort("/dev/pts/2");
    }

    private void openPort(){
        try {
            serialPort.openPort();
            serialPort.setEventsMask(SerialPort.MASK_RXCHAR);
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.addEventListener(new EventListener(), SerialPort.MASK_RXCHAR);
        } catch (SerialPortException ex) {
            System.out.println("There are an error on writing string to port т: " + ex);
        }
    }


    private void sendData(String text){
        try {
            serialPort.writeString(text);
            System.out.println("WRITE TEXT: "+text);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void onTranslateButton(){
        openPort();
        sendData(inputID.getText());
    }

    class EventListener implements SerialPortEventListener {

        public void serialEvent (SerialPortEvent event) {
            if (event.isRXCHAR () && event.getEventValue () > 0){
                try {
                    outputID.setText(serialPort.readString());
                    System.out.println("GET TEXT: "+outputID.getText());
                    serialPort.closePort();
                }
                catch (SerialPortException ex) {
                    System.out.println (ex);
                }
            }
        }
    }
}