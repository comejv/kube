package kube.view.panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.Arrays;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import kube.configuration.Config;
import kube.controller.graphical.MenuController;
import kube.view.GUI;
import kube.view.components.Buttons.MenuButton;

public class LoadingSavePanel extends JPanel {

    private int width;
    private int height;
    private JList<String> fileList;
    private MenuController buttonListener;

    public LoadingSavePanel(GUI gui, MenuController buttonListener) {

        this.buttonListener = buttonListener;

        init(gui);
    }

    public void init(GUI gui) {

        setLayout(new GridBagLayout());

        width = Math.round(Config.INIT_WIDTH / 2f);
        height = Math.round(Config.INIT_HEIGHT / 1.33f);

        setPreferredSize(new Dimension(width, height));
        
        updateList();

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        GridBagConstraints buttonPanelGBC = new GridBagConstraints();
        buttonPanelGBC.gridx = 1;
        buttonPanelGBC.gridy = 1;
        buttonPanelGBC.gridwidth = 1;
        buttonPanelGBC.weighty = 0;
        buttonPanelGBC.fill = GridBagConstraints.VERTICAL;

        MenuButton loadButton = new MenuButton("CHARGER");
        loadButton.setFont(new Font("Jomhuria", Font.PLAIN, (int) (Config.INIT_HEIGHT / 15)));
        loadButton.setPreferredSize(new Dimension(250, 100));
        loadButton.setActionCommand("load");
        loadButton.addActionListener(buttonListener);
        GridBagConstraints buttonGBC = new GridBagConstraints();
        buttonGBC.gridy = 0;
        buttonsPanel.add(loadButton, buttonGBC);
        loadButton.setEnabled(false);

        MenuButton deleteButton = new MenuButton("SUPPRIMER");
        deleteButton.setFont(new Font("Jomhuria", Font.PLAIN, (int) (Config.INIT_HEIGHT / 15)));
        deleteButton.setPreferredSize(new Dimension(250, 100));
        deleteButton.setActionCommand("delete");
        deleteButton.addActionListener(buttonListener);
        buttonGBC = new GridBagConstraints();
        buttonGBC.gridy = 1;
        buttonsPanel.add(deleteButton, buttonGBC);
        deleteButton.setEnabled(false);

        MenuButton returnButton = new MenuButton("RETOUR");
        returnButton.setFont(new Font("Jomhuria", Font.PLAIN, (int) (Config.INIT_HEIGHT / 15)));
        returnButton.setPreferredSize(new Dimension(250, 100));
        returnButton.setActionCommand("return");
        returnButton.addActionListener(buttonListener);
        buttonGBC = new GridBagConstraints();
        buttonGBC.gridy = 2;
        buttonsPanel.add(returnButton, buttonGBC);

        add(buttonsPanel, buttonPanelGBC);
    }

    public void updateList() {

        File folder = new File(Config.SAVING_PATH_DIRECTORY);
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(Config.SAVING_FILE_EXTENSION));
        String[] fileNames = Arrays.stream(listOfFiles).map(File::getName).toArray(String[]::new);

        GridBagConstraints scrollPanelGBC = new GridBagConstraints();
        scrollPanelGBC.gridy = 0;
        scrollPanelGBC.gridx = 0;
        scrollPanelGBC.gridheight = 3;
        scrollPanelGBC.weightx = 1;
        scrollPanelGBC.weighty = 1;
        scrollPanelGBC.fill = GridBagConstraints.BOTH;

        fileList = new JList<>(fileNames);
        fileList.setFont(new Font("Jomhuria", Font.PLAIN, (int) (Config.INIT_HEIGHT / 15)));

        fileList.setName("fileList");
        fileList.addMouseListener(buttonListener);

        JScrollPane scrollPane = new JScrollPane(fileList);

        add(scrollPane, scrollPanelGBC);
        revalidate();
        repaint();
    }

    public void enableLoadButton() {
        MenuButton loadButton = (MenuButton) ((JPanel) this.getComponent(1)).getComponent(0);
        loadButton.setEnabled(true);
    }

    public void enableDeleteButton() {
        MenuButton deleteButton = (MenuButton) ((JPanel) this.getComponent(1)).getComponent(1);
        deleteButton.setEnabled(true);
    }
}