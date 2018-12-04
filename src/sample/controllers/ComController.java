package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import jssc.*;
import sample.Translator;

import java.util.regex.Pattern;


public class ComController { /*Класс чтения из порта*/
    private static SerialPort serialPort; /*Создаем объект типа SerialPort*/
    private static SerialPort serialPortR; /*Создаем объект типа SerialPort*/

    String translation;

    @FXML
    TextField inputID;
    @FXML
    TextField outputID;
    @FXML
    Text outputPortID;
    @FXML
    Text inputPortID;




    public ComController(){}

    private void openPort(SerialPort port){
        try {
            port.openPort();
            port.setEventsMask(SerialPort.MASK_RXCHAR);
            port.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            //port.addEventListener(new EventListener(), SerialPort.MASK_RXCHAR);
        } catch (SerialPortException ex) {
            System.out.println("There are an error on writing string to port т: " + ex);
        }
    }


    private void sendData(String inputPort, String outputPort, String text){
        serialPort = new SerialPort(inputPort);
        serialPortR = new SerialPort(outputPort);
        inputPortID.setText("Порт для отправки: "+inputPort);
        inputPortID.setVisible(true);
        outputPortID.setText("Порт для приема: "+outputPort);
        outputPortID.setVisible(true);
        try {
            openPort(serialPort);
            openPort(serialPortR);
            serialPort.writeString(text);
            //
            String data;
            while ((data=serialPortR.readString())==null){

            }
            //System.out.println("PRINT: "+serialPortR.getPortName());
            System.out.println("Исходное слово: "+data);
            data=Translator.doTranslate(data);

            //SENDING BACK ANSWER
            serialPortR.writeString(data);
            while ((data=serialPort.readString())==null){

            }
            System.out.println("Перевод: "+data);
            outputID.setText(data);
            //
            serialPortR.closePort();
            serialPort.closePort();

        }
        catch (SerialPortException ex) {
            System.out.println("There are an error on writing string to port: " + ex);
        }
    }


    @FXML
    public void onTranslateButton(){
        String[] portNames = SerialPortList.getPortNames("/dev/pts/", Pattern.compile("[2-9]"));
        System.out.println("PORT LIST:");
        for (String s:portNames){
            System.out.println(s);
        }

        sendData(portNames[0], portNames[1],inputID.getText());
        //System.out.println("TEST: "+);

    }

    /*static class EventListener implements SerialPortEventListener {

        public void serialEvent (SerialPortEvent event) {
            if (event.isRXCHAR () && event.getEventValue () > 0){
                try {


                }
                catch (SerialPortException ex) {
                    System.out.println (ex);
                }
            }
        }
    }*/
}