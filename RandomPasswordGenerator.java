import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.security.SecureRandom;
import java.util.*;
import javax.swing.*;

public class RandomPasswordGenerator {

    public static String passwordGenerator(int length, boolean includeNum, boolean includeSym) {
        StringBuilder result = new StringBuilder();
        SecureRandom random = new SecureRandom(); // fix #5: use SecureRandom
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*_-+=/?";

        String finalPool = letters;
        if (includeNum)
            finalPool += numbers;
        if (includeSym)
            finalPool += symbols;

        int pos = 0;
        char[] password = new char[length];
        password[pos++] = letters.charAt(random.nextInt(letters.length()));
        if (includeNum && pos < length) // fix #3: removed == true
            password[pos++] = numbers.charAt(random.nextInt(numbers.length()));
        if (includeSym && pos < length) // fix #3: removed == true
            password[pos++] = symbols.charAt(random.nextInt(symbols.length()));

        for (int i = pos; i < length; i++)
            password[i] = finalPool.charAt(random.nextInt(finalPool.length()));

        List<Character> charList = new ArrayList<>();
        for (char c : password)
            charList.add(c);
        Collections.shuffle(charList, random);

        for (char c : charList)
            result.append(c);

        return result.toString();
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.setTitle("Password Generator");
        jf.setSize(400, 300);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // fix #4: restructured layout to fit all components
        JLabel title = new JLabel("Password Generator");
        title.setHorizontalAlignment(JLabel.CENTER);
        jf.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1));

        JPanel lengthPanel = new JPanel();
        JLabel lengthLabel = new JLabel("Password Length:");
        JTextField lengthField = new JTextField(5);
        lengthPanel.add(lengthLabel);
        lengthPanel.add(lengthField);

        JCheckBox numCheck = new JCheckBox("Include Numbers", true);
        JCheckBox symCheck = new JCheckBox("Include Symbols", true);
        JPanel optionsPanel = new JPanel();
        optionsPanel.add(numCheck);
        optionsPanel.add(symCheck);

        // fix #2: added output field to display the generated password
        JPanel outputPanel = new JPanel();
        JLabel outputLabel = new JLabel("Generated Password:");
        JTextField outputField = new JTextField(20);
        outputField.setEditable(false);
        outputPanel.add(outputLabel);
        outputPanel.add(outputField);

        centerPanel.add(lengthPanel);
        centerPanel.add(optionsPanel);
        centerPanel.add(outputPanel);
        jf.add(centerPanel, BorderLayout.CENTER);

        // fix #1: button added to UI with an ActionListener
        JButton generateBtn = new JButton("Generate Password");
        generateBtn.addActionListener(e -> {
            try {
                int length = Integer.parseInt(lengthField.getText().trim());
                if (length < 1) throw new NumberFormatException();
                String password = passwordGenerator(length, numCheck.isSelected(), symCheck.isSelected());
                outputField.setText(password);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(jf, "Please enter a valid positive integer for length.");
            }
        });
        jf.add(generateBtn, BorderLayout.SOUTH);

        jf.setVisible(true);
    }
}