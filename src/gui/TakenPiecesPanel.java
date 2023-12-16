package gui;

import engine.board.Alliance;
import engine.board.Move;
import engine.piece.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

/*
Panel for showing the piece that a side has taken
The white player will be shown the TODO
The black player will be shown the TODO
 */
public class TakenPiecesPanel extends JPanel {
    private static final Dimension TAKEN_PIECES_PANEL_DIMENSION = new Dimension(40,80);
    private static final Color TAKEN_PIECES_PANEL_COLOR = new Color(250, 250, 250, 190);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    private final Comparator<Piece> comparator = initComparator();

    private Comparator<Piece> initComparator() {
        return Comparator.comparingInt(Piece::getPieceValue);
    }

    protected JPanel northPanel;
    protected JPanel southPanel;
    public TakenPiecesPanel() {
        super(new BorderLayout());
        this.setPreferredSize(TAKEN_PIECES_PANEL_DIMENSION);
        this.setBackground(TAKEN_PIECES_PANEL_COLOR);
        this.setBorder(PANEL_BORDER);

        this.northPanel = new JPanel();
        this.northPanel.setLayout(new GridLayout(8,2));
        this.northPanel.setBackground(TAKEN_PIECES_PANEL_COLOR);

        this.southPanel = new JPanel();
        this.southPanel.setLayout(new GridLayout(8,2));
        this.southPanel.setBackground(TAKEN_PIECES_PANEL_COLOR);

        this.add(northPanel,BorderLayout.NORTH);
        this.add(southPanel,BorderLayout.SOUTH);
    }
    private void drawImage(List<Piece> pieces,JPanel panel) {
        for (final Piece piece: pieces) {
            File file = new File("D:\\simple\\" + piece.getPieceAlliance().toString().substring(0,1) +
                    piece.toString().substring(0,1) + ".gif");
            try {
                BufferedImage imageIcon = ImageIO.read(file);
                ImageIcon image = new ImageIcon(imageIcon);
                JLabel attackedPieceImage = new JLabel(image);
                panel.add(attackedPieceImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void redo(Table.MoveLog moveLog) {
        this.southPanel.removeAll();
        this.northPanel.removeAll();
        final List<Piece> whiteAttackedPiece = new ArrayList<>();
        final List<Piece> blackAttackedPiece = new ArrayList<>();
        for (final Move move: moveLog.getMoves()) {
            if (move.isAttack()) {
                final Piece attackedPiece = move.getAttackedPiece();
                if (attackedPiece.getPieceAlliance() == Alliance.WHITE) {
                    whiteAttackedPiece.add(attackedPiece);
                }
                else if (attackedPiece.getPieceAlliance() == Alliance.BLACK){
                    blackAttackedPiece.add(attackedPiece);
                }
                else {
                    throw new RuntimeException("Unable to reach");
                }
            }
        }
        whiteAttackedPiece.sort(comparator);
        blackAttackedPiece.sort(comparator);
        drawImage(whiteAttackedPiece,northPanel);
        drawImage(blackAttackedPiece,southPanel);
        validate();
    }
}
