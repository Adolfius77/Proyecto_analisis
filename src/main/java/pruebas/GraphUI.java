package pruebas;

import implementaciones.Grafo; // Asegúrate que esta importación sea correcta

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
// Ya no necesitamos java.net.URL ya que no cargamos iconos

public class GraphUI extends JFrame {

    // Lógica del Grafo
    private Grafo grafo;

    // Componentes de la UI
    private JRadioButton rDirigido, rPonderado;
    private JButton btnCrearGrafo;
    private JPanel panelConfiguracion, panelOperaciones;
    private JTextPane areaSalida; // Usamos JTextPane para soportar colores

    // Campos de texto y botones
    private JTextField txtVertice, txtOrigen, txtDestino, txtPeso;
    private JButton btnInsertarVertice, btnEliminarVertice, btnInsertarArista, btnEliminarArista, btnActualizarPeso;
    private JButton btnGrado, btnAdyacentes, btnMatriz, btnInfoGeneral;

    // Colores y Estilos
    private final Color COLOR_FONDO = new Color(245, 245, 245);
    private final Color COLOR_BOTON = new Color(70, 130, 180);
    private final Color COLOR_TEXTO_BOTON = Color.WHITE;
    private final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 12);

    // Enumeración para tipos de log
    private enum LogType { INFO, SUCCESS, ERROR }

    public GraphUI() {
        initUI();
    }

    private void initUI() {
        // --- Aplicar Look and Feel Moderno ---
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // --- Configuración de la ventana principal ---
        setTitle("GraphMaster Pro 📈 - Visualizador de Grafos");
        setSize(900, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Paneles Principales ---
        panelConfiguracion = createConfigPanel(); // Asignar a la variable de instancia
        add(panelConfiguracion, BorderLayout.NORTH);
        add(createOperationsPanel(), BorderLayout.CENTER);
        add(createOutputPanel(), BorderLayout.SOUTH);

        setupActionListeners();
        setPanelOperacionesEnabled(false);

        setVisible(true);
    }

    private JPanel createConfigPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        panel.setBorder(BorderFactory.createTitledBorder("1. Configuración del Grafo"));

        rDirigido = new JRadioButton("Dirigido", true);
        JRadioButton rNoDirigido = new JRadioButton("No Dirigido");
        ButtonGroup grupoDirigido = new ButtonGroup();
        grupoDirigido.add(rDirigido);
        grupoDirigido.add(rNoDirigido);

        rPonderado = new JRadioButton("Ponderado", true);
        JRadioButton rNoPonderado = new JRadioButton("No Ponderado");
        ButtonGroup grupoPonderado = new ButtonGroup();
        grupoPonderado.add(rPonderado);
        grupoPonderado.add(rNoPonderado);

        btnCrearGrafo = createStyledButton("Crear y Empezar");

        panel.add(new JLabel("Tipo:"));
        panel.add(rDirigido);
        panel.add(rNoDirigido);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(new JLabel("Peso:"));
        panel.add(rPonderado);
        panel.add(rNoPonderado);
        panel.add(btnCrearGrafo);
        return panel;
    }

    private JPanel createOperationsPanel() {
        panelOperaciones = new JPanel();
        panelOperaciones.setLayout(new BoxLayout(panelOperaciones, BoxLayout.Y_AXIS));
        panelOperaciones.setBorder(BorderFactory.createTitledBorder("2. Operaciones"));

        // Panel de Vértices
        JPanel panelVertices = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelVertices.setBorder(BorderFactory.createTitledBorder("Vértices"));
        txtVertice = new JTextField(8);
        btnInsertarVertice = createStyledButton("Insertar");
        btnEliminarVertice = createStyledButton("Eliminar");
        btnGrado = createStyledButton("Grado");
        btnAdyacentes = createStyledButton("Adyacentes");
        panelVertices.add(new JLabel("Vértice:"));
        panelVertices.add(txtVertice);
        panelVertices.add(btnInsertarVertice);
        panelVertices.add(btnEliminarVertice);
        panelVertices.add(btnGrado);
        panelVertices.add(btnAdyacentes);

        // Panel de Aristas
        JPanel panelAristas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAristas.setBorder(BorderFactory.createTitledBorder("Aristas"));
        txtOrigen = new JTextField(8);
        txtDestino = new JTextField(8);
        txtPeso = new JTextField(5);
        btnInsertarArista = createStyledButton("Insertar Arista");
        btnEliminarArista = createStyledButton("Eliminar Arista");
        btnActualizarPeso = createStyledButton("Actualizar Peso");
        panelAristas.add(new JLabel("Origen:"));
        panelAristas.add(txtOrigen);
        panelAristas.add(new JLabel("Destino:"));
        panelAristas.add(txtDestino);
        panelAristas.add(new JLabel("Peso:"));
        panelAristas.add(txtPeso);
        panelAristas.add(btnInsertarArista);
        panelAristas.add(btnEliminarArista);
        panelAristas.add(btnActualizarPeso);

        // Panel de Información General
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInfo.setBorder(BorderFactory.createTitledBorder("Información General del Grafo"));
        btnInfoGeneral = createStyledButton("Contar Vértices/Aristas");
        btnMatriz = createStyledButton("Mostrar Matriz de Adyacencia");
        panelInfo.add(btnInfoGeneral);
        panelInfo.add(btnMatriz);

        panelOperaciones.add(panelVertices);
        panelOperaciones.add(panelAristas);
        panelOperaciones.add(panelInfo);
        return panelOperaciones;
    }

    private JScrollPane createOutputPanel() {
        areaSalida = new JTextPane();
        areaSalida.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaSalida.setEditable(false);
        areaSalida.setBackground(new Color(250, 250, 250));
        JScrollPane scrollPane = new JScrollPane(areaSalida);
        scrollPane.setBorder(BorderFactory.createTitledBorder("3. Consola de Salida"));
        scrollPane.setPreferredSize(new Dimension(800, 250));
        return scrollPane;
    }

    // Método createStyledButton ahora solo toma el texto (sin iconName)
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        styleButton(button);
        return button;
    }

    private void styleButton(JButton button) {
        button.setBackground(COLOR_BOTON);
        button.setForeground(COLOR_TEXTO_BOTON);
        button.setFont(FUENTE_BOTON);
        button.setFocusPainted(false);
    }

    private void setPanelOperacionesEnabled(boolean enabled) {
        for (Component comp : panelOperaciones.getComponents()) {
            if (comp instanceof JPanel) {
                for (Component innerComp : ((JPanel) comp).getComponents()) {
                    innerComp.setEnabled(enabled);
                }
            }
        }
    }

    private void setupActionListeners() {
        btnCrearGrafo.addActionListener(this::crearGrafo);
        btnInsertarVertice.addActionListener(e -> handleAction(this::accionInsertarVertice));
        btnEliminarVertice.addActionListener(e -> handleAction(this::accionEliminarVertice));
        btnGrado.addActionListener(e -> handleAction(this::accionVerGrado));
        btnAdyacentes.addActionListener(e -> handleAction(this::accionVerAdyacentes));
        btnInsertarArista.addActionListener(e -> handleAction(this::accionInsertarArista));
        btnEliminarArista.addActionListener(e -> handleAction(this::accionEliminarArista));
        btnActualizarPeso.addActionListener(e -> handleAction(this::accionActualizarPeso));

        btnInfoGeneral.addActionListener(e -> handleAction(() -> log(grafo.obtenerNumeroVerticesYAristas(), LogType.INFO)));
        btnMatriz.addActionListener(e -> handleAction(() -> log(grafo.obtenerMatrizDeAdyacencia(), LogType.INFO)));
    }

    private void crearGrafo(ActionEvent e) {
        grafo = new Grafo(rDirigido.isSelected(), rPonderado.isSelected());

        // Deshabilita los componentes del panel de configuración usando la variable de instancia.
        for (Component c : panelConfiguracion.getComponents()) {
            c.setEnabled(false);
        }

        setPanelOperacionesEnabled(true);
        txtPeso.setEnabled(grafo.esPonderado());
        btnActualizarPeso.setEnabled(grafo.esPonderado());

        areaSalida.setText("");
        log("Grafo creado. ¡Listo para operar!", LogType.SUCCESS);
        JOptionPane.showMessageDialog(this, "El grafo ha sido creado con éxito.", "Creación Exitosa", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleAction(Runnable action) {
        if (grafo == null) {
            log("Error: Primero debes crear un grafo.", LogType.ERROR);
            return;
        }
        try {
            action.run();
        } catch (Exception ex) {
            log("Ha ocurrido un error inesperado: " + ex.getMessage(), LogType.ERROR);
            ex.printStackTrace();
        }
    }

    // --- Métodos de Acción ---

    private void accionInsertarVertice() {
        String vertice = txtVertice.getText().trim();
        if (!vertice.isEmpty()) {
            log(grafo.insertarVertice(vertice), LogType.INFO);
            txtVertice.setText("");
        } else {
            log("El campo 'Vértice' no puede estar vacío.", LogType.ERROR);
        }
    }

    private void accionEliminarVertice() {
        String vertice = txtVertice.getText().trim();
        if (!vertice.isEmpty()) {
            log(grafo.eliminarVertice(vertice), LogType.INFO);
            txtVertice.setText("");
        } else {
            log("El campo 'Vértice' no puede estar vacío.", LogType.ERROR);
        }
    }

    private void accionVerGrado() {
        String vertice = txtVertice.getText().trim();
        if (!vertice.isEmpty()) {
            log(grafo.obtenerGradoVertice(vertice), LogType.INFO);
        } else {
            log("El campo 'Vértice' no puede estar vacío.", LogType.ERROR);
        }
    }

    private void accionVerAdyacentes() {
        String vertice = txtVertice.getText().trim();
        if (!vertice.isEmpty()) {
            log(grafo.obtenerAdyacencias(vertice), LogType.INFO);
        } else {
            log("El campo 'Vértice' no puede estar vacío.", LogType.ERROR);
        }
    }

    private void accionInsertarArista() {
        String origen = txtOrigen.getText().trim();
        String destino = txtDestino.getText().trim();
        if (origen.isEmpty() || destino.isEmpty()) {
            log("Los campos 'Origen' y 'Destino' no pueden estar vacíos.", LogType.ERROR);
            return;
        }

        int peso = 1;
        if (grafo.esPonderado()) {
            try {
                peso = Integer.parseInt(txtPeso.getText().trim());
            } catch (NumberFormatException e) {
                log("El peso debe ser un número entero válido.", LogType.ERROR);
                return;
            }
        }
        log(grafo.insertarArista(origen, destino, peso), LogType.INFO);
        txtOrigen.setText("");
        txtDestino.setText("");
        txtPeso.setText("");
    }

    private void accionEliminarArista() {
        String origen = txtOrigen.getText().trim();
        String destino = txtDestino.getText().trim();
        if (!origen.isEmpty() && !destino.isEmpty()) {
            log(grafo.eliminarArista(origen, destino), LogType.INFO);
            txtOrigen.setText("");
            txtDestino.setText("");
        } else {
            log("Los campos 'Origen' y 'Destino' no pueden estar vacíos.", LogType.ERROR);
        }
    }

    private void accionActualizarPeso() {
        String origen = txtOrigen.getText().trim();
        String destino = txtDestino.getText().trim();
        String pesoStr = txtPeso.getText().trim();
        if (origen.isEmpty() || destino.isEmpty() || pesoStr.isEmpty()) {
            log("Los campos 'Origen', 'Destino' y 'Peso' son obligatorios.", LogType.ERROR);
            return;
        }
        try {
            int nuevoPeso = Integer.parseInt(pesoStr);
            log(grafo.actualizarPonderacion(origen, destino, nuevoPeso), LogType.INFO);
            txtOrigen.setText("");
            txtDestino.setText("");
            txtPeso.setText("");
        } catch (NumberFormatException e) {
            log("El nuevo peso debe ser un número entero.", LogType.ERROR);
        }
    }

    // --- Lógica para Escribir en el JTextPane con Colores ---

    private void log(String message, LogType type) {
        if (message == null) return; // Evita errores si el método del grafo retorna null

        StyledDocument doc = areaSalida.getStyledDocument();
        SimpleAttributeSet style = new SimpleAttributeSet();

        switch (type) {
            case SUCCESS:
                StyleConstants.setForeground(style, new Color(0, 128, 0)); // Verde
                break;
            case ERROR:
                StyleConstants.setForeground(style, Color.RED);
                break;
            case INFO:
            default:
                StyleConstants.setForeground(style, Color.BLACK);
                break;
        }

        try {
            doc.insertString(doc.getLength(), ">> " + message + "\n", style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        // Auto-scroll hacia el final
        areaSalida.setCaretPosition(doc.getLength());
    }

    public static void main(String[] args) {
        // Ejecuta la UI en el hilo de despacho de eventos de Swing
        SwingUtilities.invokeLater(GraphUI::new);
    }
}