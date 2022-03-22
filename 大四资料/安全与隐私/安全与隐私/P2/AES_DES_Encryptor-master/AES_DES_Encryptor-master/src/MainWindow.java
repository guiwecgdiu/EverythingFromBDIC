import com.alee.extended.painter.TitledBorderPainter;
import com.alee.extended.window.WebProgressDialog;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.radiobutton.WebRadioButton;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.text.WebTextField;
import com.sun.deploy.util.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;


public class MainWindow extends WebFrame {
    MainWindow() {

        //界面布局
        this.setTitle("DES and AES");
        this.setSize(640, 420);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setVgap(10);
        WebPanel container = new WebPanel(borderLayout);
        container.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setContentPane(container);

        WebPanel radioPanel = new WebPanel(new FlowLayout());
        container.add(radioPanel, BorderLayout.NORTH);
        TitledBorderPainter painter = new TitledBorderPainter("choosing algorithm");
        painter.setMargin(5);
        radioPanel.setPainter(painter);

        WebRadioButton desButton = new WebRadioButton("DES");
        desButton.setSelected(true);
        radioPanel.add(desButton);
        WebRadioButton aesButton = new WebRadioButton("AES");
        radioPanel.add(aesButton);

        ButtonGroup group = new ButtonGroup();
        group.add(desButton);
        group.add(aesButton);

        WebPanel fileEncryptionPanel = new WebPanel(new GridBagLayout());
        container.add(fileEncryptionPanel, BorderLayout.CENTER);
        painter = new TitledBorderPainter("File");
        painter.setMargin(5);
        fileEncryptionPanel.setPainter(painter);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.ipadx = 202;
        c.gridwidth = 100;

        WebLabel sourceFileLabel = new WebLabel("plain file:", WebLabel.LEADING);
        fileEncryptionPanel.add(sourceFileLabel, c);

        WebTextField sourceFileField = new WebTextField(30);
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 2;
        fileEncryptionPanel.add(sourceFileField, c);

        WebButton sourceFileChooseButton = new WebButton("choose plain file");
        sourceFileChooseButton.setRound(9);
        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 1;
        c.ipadx = 0;
        fileEncryptionPanel.add(sourceFileChooseButton, c);

        WebLabel encryptedFileLabel = new WebLabel("encrypted file:", WebLabel.LEADING);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.ipadx = 20;
        fileEncryptionPanel.add(encryptedFileLabel, c);

        WebTextField encryptedFileField = new WebTextField(30);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        fileEncryptionPanel.add(encryptedFileField, c);

        WebButton encryptedFileChooseButton = new WebButton("choose encrypted file");
        c.gridx = 3;
        c.gridy = 1;
        c.ipadx = 0;
        c.gridwidth = 1;
        encryptedFileChooseButton.setRound(9);
        fileEncryptionPanel.add(encryptedFileChooseButton, c);

        WebLabel keyLabel = new WebLabel("key:");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;

        fileEncryptionPanel.add(keyLabel, c);

        WebTextField keyField = new WebTextField("0123456789abcdef", 30);
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;
        fileEncryptionPanel.add(keyField, c);

        WebButton encryptButton = new WebButton("Encrypt");
        encryptButton.setRound(9);
        c.gridx = 3;
        c.gridy = 2;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        fileEncryptionPanel.add(encryptButton, c);

        WebButton decryptButton = new WebButton("Decrypt");
        decryptButton.setRound(9);
        c.gridx = 3;
        c.gridy = 3;
        fileEncryptionPanel.add(decryptButton, c);

        WebLabel speedLabel = new WebLabel("speed:", WebLabel.LEADING);
        c.gridx = 0;
        c.gridy = 3;
        fileEncryptionPanel.add(speedLabel, c);

        WebLabel speedInfoLabel = new WebLabel(WebLabel.LEADING);
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 2;
        fileEncryptionPanel.add(speedInfoLabel, c);

        WebPanel testPanel = new WebPanel(new GridBagLayout());
        TitledBorderPainter titledBorderPainter = new TitledBorderPainter("text");
        testPanel.setPainter(titledBorderPainter);
        container.add(testPanel, BorderLayout.SOUTH);
        c = new GridBagConstraints();

        WebLabel sourceTextLabel = new WebLabel("plain text:");
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        testPanel.add(sourceTextLabel, c);

        WebTextField sourceTextField = new WebTextField(30);
        c.gridx = 1;
        c.gridy = 0;
        testPanel.add(sourceTextField, c);

        WebLabel encryptedTextLabel = new WebLabel("encrypted text:");
        c.gridx = 0;
        c.gridy = 1;
        testPanel.add(encryptedTextLabel, c);

        WebTextField encryptedTextField = new WebTextField(30);
        c.gridx = 1;
        c.gridy = 1;
        testPanel.add(encryptedTextField, c);

        WebButton encryptionTestButton = new WebButton("Encrypt");
        encryptionTestButton.setRound(9);
        c.gridx = 2;
        c.gridy = 1;
        testPanel.add(encryptionTestButton, c);

        WebLabel keyLabel2 = new WebLabel("key:");
        c.gridx = 0;
        c.gridy = 2;
        testPanel.add(keyLabel2, c);

        WebTextField keyField2 = new WebTextField(30);
        c.gridx = 1;
        c.gridy = 2;
        testPanel.add(keyField2, c);

        WebButton decryptionTestButton = new WebButton("Decrypt");
        decryptionTestButton.setRound(9);
        c.gridx = 2;
        c.gridy = 2;
        testPanel.add(decryptionTestButton, c);


        //处理按钮事件

        desButton.addActionListener(e -> keyField.setText("0123456789abcdef"));
        aesButton.addActionListener(e -> keyField.setText("0123456789abcdef0123456789abcdef"));

        //选择源文件
        sourceFileChooseButton.addActionListener(e -> {
            WebFileChooser fileChooser = new WebFileChooser(".");
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.showOpenDialog(this);
            File file = fileChooser.getSelectedFile();
            if (file != null) {
                sourceFileField.setText(file.getAbsolutePath());
                StringBuilder fileName = new StringBuilder(file.getName());
                String[] s = fileName.toString().split("\\.");
                fileName = new StringBuilder();
                for (int i = 0; i < s.length - 1; i++) {
                    fileName.append(s[i]);
                    fileName.append(".");
                }
                fileName.append("encrypted.");
                fileName.append(s[s.length - 1]);
                Path encryptedFilePath = Paths.get(file.getAbsolutePath()).getParent();
                encryptedFilePath = encryptedFilePath.resolve(fileName.toString());
                encryptedFileField.setText(encryptedFilePath.toString());
            }
        });

        //选择加密文件
        encryptedFileChooseButton.addActionListener(e -> {
            WebFileChooser fileChooser = new WebFileChooser(".");
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.showOpenDialog(this);
            File file = fileChooser.getSelectedFile();
            if (file != null) {
                encryptedFileField.setText(file.getAbsolutePath());
                StringBuilder fileName = new StringBuilder(file.getName());
                String[] s = fileName.toString().split("\\.");
                fileName = new StringBuilder();
                for (int i = 0; i < s.length - 1; i++) {
                    fileName.append(s[i]);
                    fileName.append(".");
                }
                fileName.append("src.");
                fileName.append(s[s.length - 1]);
                Path sourceFilePath = Paths.get(file.getAbsolutePath());
                sourceFilePath = sourceFilePath.getParent().resolve(fileName.toString());
                sourceFileField.setText(sourceFilePath.toString());
            }
        });

        //加密文件
        encryptButton.addActionListener(e -> {
            String keyString = keyField.getText();

            try {
                byte[] key = DatatypeConverter.parseHexBinary(keyString);
                if (desButton.isSelected() && key.length != 8) {
                    WebOptionPane.showMessageDialog(this, "密钥长度"+key.length, "错误", WebOptionPane.ERROR_MESSAGE);
                    return;
                } else if (aesButton.isSelected() && key.length != 16) {
                    WebOptionPane.showMessageDialog(this, "密钥长度"+key.length, "错误", WebOptionPane.ERROR_MESSAGE);
                    return;
                }

                File sourceFile = new File(sourceFileField.getText());
                if (sourceFile.exists() & !sourceFile.isDirectory())
                    new Thread(() -> {
                        int length = desButton.isSelected() ? 8 : 16;
                        byte[] buff = new byte[length];
                        aesButton.setEnabled(false);
                        desButton.setEnabled(false);
                        try (FileInputStream in = new FileInputStream(new File(sourceFileField.getText()));
                             FileOutputStream out = new FileOutputStream(new File(encryptedFileField.getText()))) {
                            long sourceFileLength = new File(sourceFileField.getText()).length();
                            buff[0] = (byte) (sourceFileLength % length);
                            if (buff[0] == 0)
                                buff[0] = (byte) length;
                            if (desButton.isSelected())
                                out.write(DesEncryptor.encrypt(buff, key));
                            else
                                out.write(AesEncryptor.encrypt(buff, key));
                            WebProgressDialog progressDialog = new WebProgressDialog(this, "进度");
                            progressDialog.setVisible(true);
                            long time = System.currentTimeMillis();
                            for (int i = in.read(buff), bytesRead = 0; i != -1; i = in.read(buff)) {
                                bytesRead += i;
                                progressDialog.setProgress(Math.round(bytesRead / (float) sourceFileLength * 100));
                                if (desButton.isSelected())
                                    out.write(DesEncryptor.encrypt(buff, key));
                                else
                                    out.write(AesEncryptor.encrypt(buff, key));
                            }
                            time = System.currentTimeMillis() - time + 1;
                            double speed = sourceFileLength / time * 1000;
                            speedInfoLabel.setText(Math.round(speed) + " B/s");
                            progressDialog.dispose();
                            WebOptionPane.showMessageDialog(this, "加密完成", "加密完成", WebOptionPane.INFORMATION_MESSAGE);

                        } catch (IOException e1) {
                            e1.printStackTrace();
                            WebOptionPane.showMessageDialog(this, e1.getMessage(), "加密失败", WebOptionPane.ERROR_MESSAGE);
                        } finally {
                            aesButton.setEnabled(true);
                            desButton.setEnabled(true);
                        }
                    }).start();

                else WebOptionPane.showMessageDialog(this, "源文件无法读取", "错误", WebOptionPane.ERROR_MESSAGE);
            } catch (java.lang.IllegalArgumentException ee) {
                //捕捉key的格式不正确的Exception
                WebOptionPane.showMessageDialog(this, ee.getMessage(), "错误", WebOptionPane.ERROR_MESSAGE);
            }
        });

        //解密文件
        decryptButton.addActionListener(e -> {
            String keyss = keyField.getText();
            String keyString = String.format("%16s",keyss);
            keyString = keyString.replaceAll("\\s","0");
            try {
                byte[] key = DatatypeConverter.parseHexBinary(keyString);
                if (desButton.isSelected() && key.length != 8) {

                    WebOptionPane.showMessageDialog(this, "密钥长度"+key.length, "错误", WebOptionPane.ERROR_MESSAGE);
                    return;
                } else if (aesButton.isSelected() && key.length != 16) {
                    WebOptionPane.showMessageDialog(this, "密钥长度是"+key.length+"字节", "错误", WebOptionPane.ERROR_MESSAGE);
                    return;
                }
                File encryptedFile = new File(encryptedFileField.getText());
                if (encryptedFile.exists() & !encryptedFile.isDirectory()) {
                    new Thread(() -> {
                        int length = desButton.isSelected() ? 8 : 16;
                        byte[] buff = new byte[length];
                        aesButton.setEnabled(false);
                        desButton.setEnabled(false);
                        try (FileOutputStream out = new FileOutputStream(new File(sourceFileField.getText()));
                             FileInputStream in = new FileInputStream(new File(encryptedFileField.getText()))) {
                            long time = System.currentTimeMillis();
                            if (in.read(buff) != length) {
                                WebOptionPane.showMessageDialog(this, "不是有效的加密文件", "解密失败", WebOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            WebProgressDialog progressDialog = new WebProgressDialog(this, "进度");
                            progressDialog.setVisible(true);
                            if (desButton.isSelected())
                                buff = DesEncryptor.decrypt(buff, key);
                            else
                                buff = AesEncryptor.decrypt(buff, key);
                            int lastBlockLength = buff[0];
                            for (int i = in.read(buff), bytesRead = length; i != -1; i = in.read(buff)) {
                                bytesRead += i;
                                progressDialog.setProgress(Math.round(bytesRead / (float) encryptedFile.length() * 100));
                                byte[] result;
                                if (desButton.isSelected())
                                    result = DesEncryptor.decrypt(buff, key);
                                else
                                    result = AesEncryptor.decrypt(buff, key);
                                if (in.available() == 0)
                                    out.write(result, 0, lastBlockLength);
                                else
                                    out.write(result);
                            }
                            time = System.currentTimeMillis() - time + 1;
                            double speed = encryptedFile.length() / time * 1000;
                            speedInfoLabel.setText(Math.round(speed) + " B/s");
                            progressDialog.dispose();
                            WebOptionPane.showMessageDialog(this, "解密完成", "解密完成", WebOptionPane.INFORMATION_MESSAGE);

                        } catch (IOException e1) {
                            WebOptionPane.showMessageDialog(this, "解密失败", "解密失败", WebOptionPane.ERROR_MESSAGE);
                            e1.printStackTrace();
                        } finally {
                            aesButton.setEnabled(true);
                            desButton.setEnabled(true);
                        }
                    }).start();
                } else
                    WebOptionPane.showMessageDialog(this, "加密文件无法读取", "错误", WebOptionPane.ERROR_MESSAGE);
            } catch (java.lang.IllegalArgumentException ee) {
                WebOptionPane.showMessageDialog(this, ee.getMessage(), "错误", WebOptionPane.ERROR_MESSAGE);
            }
        });


        //加密测试
        encryptionTestButton.addActionListener(e -> {
            try {
                String keyss11 = sourceTextField.getText();
                String s = String.format("%16s",keyss11);
                s = s.replaceAll("\\s","0");
                byte[] sourceBytes = DatatypeConverter.parseHexBinary(s);
                byte[] key = DatatypeConverter.parseHexBinary(keyField2.getText());
                if (sourceBytes.length == 0) {
                    WebOptionPane.showMessageDialog(this, "明文为空", "错误", WebOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (key.length == 0) {
                    WebOptionPane.showMessageDialog(this, "密钥为空", "错误", WebOptionPane.ERROR_MESSAGE);
                    return;
                }
                long time2 = System.currentTimeMillis();
                byte[] inputBytes = new byte[8];
                if (sourceBytes.length < inputBytes.length)
                    System.arraycopy(sourceBytes, 0, inputBytes, 0, sourceBytes.length);
                else
                    System.arraycopy(sourceBytes, 0, inputBytes, 0, inputBytes.length);
                byte[] encryptedBytes = DesEncryptor.encrypt(inputBytes, key) ;
                String encryptedText = DatatypeConverter.printHexBinary(encryptedBytes);
                encryptedTextField.setText(encryptedText);
                time2 = System.currentTimeMillis() - time2 + 1;

                WebOptionPane.showMessageDialog(this, "Time: "+time2, "speed", WebOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ee) {
                WebOptionPane.showMessageDialog(this, ee.getMessage(), "错误", WebOptionPane.ERROR_MESSAGE);
            }
        });

        //解密测试
        decryptionTestButton.addActionListener(e -> {
            try {
                String keyss1 = encryptedTextField.getText();
                String d = String.format("%16s",keyss1);
                d = d.replaceAll("\\s","0");
                byte[] encryptedBytes = DatatypeConverter.parseHexBinary(d);
                byte[] keyBytes = DatatypeConverter.parseHexBinary(keyField2.getText());
                if (encryptedBytes.length != 8) {
                    WebOptionPane.showMessageDialog(this, "密文不是8字节", "错误", WebOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (keyBytes.length == 0) {
                    WebOptionPane.showMessageDialog(this, "密钥为空", "错误", WebOptionPane.ERROR_MESSAGE);
                    return;
                }
                long time1 = System.currentTimeMillis();
                byte[] sourceBytes = DesEncryptor.decrypt(encryptedBytes, keyBytes) ;
                sourceTextField.setText(DatatypeConverter.printHexBinary(sourceBytes));
                time1 = System.currentTimeMillis() - time1 + 1;

                WebOptionPane.showMessageDialog(this, "Time: "+time1, "speed", WebOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ee) {
                WebOptionPane.showMessageDialog(this, ee.getMessage(), "错误", WebOptionPane.ERROR_MESSAGE);
            }

        });

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }


    public static void setUIFont() {
        //设置界面字体
        Font font = new Font("微软雅黑", Font.PLAIN, 13);
        WebLookAndFeel.globalControlFont = font;
        WebLookAndFeel.globalAcceleratorFont = new Font("微软雅黑", Font.PLAIN, 12);
        WebLookAndFeel.globalTitleFont = font;
        WebLookAndFeel.globalTextFont = font;
        WebLookAndFeel.globalTooltipFont = font;
        WebLookAndFeel.toolTipFont = font;
        FontUIResource f = new FontUIResource("微软雅黑", Font.PLAIN, 13);
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        setUIFont();
        WebLookAndFeel.initializeManagers();
        WebLookAndFeel.install();
        new MainWindow();
    }

}
