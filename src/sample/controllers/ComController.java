package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import jssc.SerialPort;  /*Импорт классов библиотеки jssc*/
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class ComController {  /*Класс чтения из порта*/
    private static SerialPort serialPort; /*Создаем объект типа SerialPort*/

    @FXML
    TextField inputID;
    @FXML
    TextField outputID;
    @FXML
    TextField portID;
    @FXML
    static TextField tmpID;

    public TextField getTmpID() {
        return tmpID;
    }

    public void setTmpID(TextField tmpID) {
        this.tmpID = tmpID;
    }

    public ComController(){}

    @FXML
    public void onTranslateButton(){
        // TODO Auto-generated method stub
        serialPort = new SerialPort(portID.getText());
        try {
            serialPort.openPort();

            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);


            serialPort.addEventListener(new EventListener(), SerialPort.MASK_RXCHAR);

            serialPort.writeString("Hurrah!");
            //tmpID.setText(serialPort.readString());
            //System.out.println("done"+tmpID.getText());
            serialPort.closePort();
        }
        catch (SerialPortException ex) {
            System.out.println("There are an error on writing string to port т: " + ex);
        }
    }


    static class EventListener implements SerialPortEventListener { /*Слушатель срабатывающий по появлению данных на COM-порт*/
        public void serialEvent (SerialPortEvent event) {
            if (event.isRXCHAR () && event.getEventValue () > 0){
                try {
                    System.out.println("PRINT: "+serialPort.getPortName());
                    String data = serialPort.readString (event.getEventValue());
                    System.out.print (data);
                    tmpID.setText(data);
                    //serialPort.closePort();
                }
                catch (SerialPortException ex) {
                    System.out.println (ex);
                }
            }
        }
    }
}