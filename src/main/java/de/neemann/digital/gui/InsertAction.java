package de.neemann.digital.gui;

import de.neemann.digital.draw.elements.VisualElement;
import de.neemann.digital.draw.graphics.Vector;
import de.neemann.digital.draw.library.ElementLibrary;
import de.neemann.digital.draw.library.LibraryNode;
import de.neemann.digital.draw.shapes.ShapeFactory;
import de.neemann.digital.gui.components.CircuitComponent;
import de.neemann.digital.lang.Lang;
import de.neemann.gui.ErrorMessage;
import de.neemann.gui.ToolTipAction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Action to insert the given node to the given circuit
 * Created by hneemann on 25.03.17.
 */
public final class InsertAction extends ToolTipAction {
    private final InsertHistory insertHistory;
    private final CircuitComponent circuitComponent;
    private final ShapeFactory shapeFactory;
    private LibraryNode node;

    /**
     * Creates a new instance
     *
     * @param node             the node which holds the element to add
     * @param insertHistory    the history to add the element to
     * @param circuitComponent the component to add the element to
     * @param shapeFactory     the shapeFactory to create the icon
     */
    public InsertAction(LibraryNode node, InsertHistory insertHistory, CircuitComponent circuitComponent, ShapeFactory shapeFactory) {
        super(node.getTranslatedName(), node.getIconOrNull(shapeFactory));
        this.shapeFactory = shapeFactory;
        this.node = node;
        this.insertHistory = insertHistory;
        this.circuitComponent = circuitComponent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        VisualElement visualElement = new VisualElement(node.getName()).setPos(new Vector(10, 10)).setShapeFactory(shapeFactory);
        circuitComponent.setPartToInsert(visualElement);
        if (getIcon() == null) {
            try {
                node.getDescription();
                setIcon(node.getIcon(shapeFactory));
            } catch (IOException ex) {
                SwingUtilities.invokeLater(new ErrorMessage(Lang.get("msg_errorImportingModel")).addCause(ex));
            }
        }
        insertHistory.add(this);
    }

    /**
     * @return true if element to insert is a custom element
     */
    public boolean isCustom() {
        return node.getDescriptionOrNull() instanceof ElementLibrary.ElementTypeDescriptionCustom;
    }

    /**
     * @return the name of the node to insert
     */
    public String getName() {
        return node.getName();
    }

    /**
     * Updates this action to a new node
     *
     * @param node the node
     */
    public void update(LibraryNode node) {
        this.node = node;
        try {
            final ImageIcon icon = node.getIcon(shapeFactory);
            setIcon(icon);
        } catch (IOException ex) {
            SwingUtilities.invokeLater(new ErrorMessage(Lang.get("msg_errorImportingModel")).addCause(ex));
        }
    }
}
