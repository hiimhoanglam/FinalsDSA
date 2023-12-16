package gui;
import engine.board.*;
import engine.piece.Piece;
import engine.player.MoveTransition;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class Table {
    JFrame gameFrame;
    private final GameHistoryPanel gameHistoryPanel;
    private final TakenPiecesPanel takenPiecesPanel;

    private static final Dimension OUTER_FRAME = new Dimension(600,600);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(500,500);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    private static final String FILE_ICON_PATH = "D:\\chesspieces\\";
    private static final Color lightColor = new Color(177, 228, 185);
    private static final Color darkColor = new Color(112, 162, 163);
    private BoardDirection boardDirection;
    private Board chessBoard;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private MoveLog moveLog;
    private final BoardPanel boardPanel;
    private boolean highlightLegal;
    public Table() {
        this.chessBoard = Board.initBoard();
        this.boardDirection = BoardDirection.NORMAL;
        this.boardPanel = new BoardPanel();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.moveLog = new MoveLog();
        this.highlightLegal = false;

        this.gameFrame = new JFrame("Finals DSA Projects");
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.add(takenPiecesPanel,BorderLayout.WEST);
        this.gameFrame.add(boardPanel,BorderLayout.CENTER);
        this.gameFrame.add(gameHistoryPanel,BorderLayout.EAST);

        final JMenuBar menubar = new JMenuBar();
        populateMenuBar(menubar);
        gameFrame.setJMenuBar(menubar);

        this.gameFrame.setVisible(true);
        this.gameFrame.setSize(OUTER_FRAME);
        this.gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
    private void clearTile() {
        sourceTile = null;
        destinationTile = null;
        humanMovedPiece = null;
    }
    private void populateMenuBar(JMenuBar menubar) {
        menubar.add(createFileMenu());
        menubar.add(createPreferencesMenu());
    }

    private JMenu createFileMenu() {
        final JMenu menu = new JMenu("Load chess game");
        final JMenuItem pgn = new JMenuItem("Load PGN");
        pgn.addActionListener(e -> System.out.println("Loading PGN file"));
        final JMenuItem fen = new JMenuItem("Load FEN");
        fen.addActionListener(e -> System.out.println("Loading FEN file"));
        final JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));
        menu.add(pgn);
        menu.add(fen);
        menu.add(exit);
        return menu;
    }
    private JMenu createPreferencesMenu() {
        final JMenu preferences = new JMenu("Preferences");
        final JMenuItem flip = new JMenuItem("Flip board");
        flip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.getOpposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferences.add(flip);
        preferences.addSeparator();
        final JCheckBoxMenuItem highlightCheckBox = new JCheckBoxMenuItem("Highlight legal moves",false);
        highlightCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightLegal = highlightCheckBox.isSelected();
            }
        });
        preferences.add(highlightCheckBox);
        return preferences;
    }
    public enum BoardDirection {
        NORMAL {
            @Override
            BoardDirection getOpposite() {
                return FLIPPED;
            }

            @Override
            List<TilePanel> traverse(final List<TilePanel> tileList) {
                return tileList;
            }
        },
        FLIPPED {
            @Override
            BoardDirection getOpposite() {
                return NORMAL;
            }

            @Override
            List<TilePanel> traverse(final List<TilePanel> tileList) {
                Collections.reverse(tileList);
                return tileList;
            }
        };
        abstract BoardDirection getOpposite();
        abstract List<TilePanel> traverse(final List<TilePanel> tileList);
    }
    public class BoardPanel extends JPanel {
        final List<TilePanel> tileList;
        public BoardPanel() {
            super(new GridLayout(8,8));
            tileList = new ArrayList<>();
            this.setPreferredSize(BOARD_PANEL_DIMENSION);
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                TilePanel tilePanel = new TilePanel(this,i);
                tilePanel.assignTileColor();
                tilePanel.assignPieceIcon(chessBoard);
                tileList.add(tilePanel);

            }
            validate();
            repaint();
        }

        public void drawBoard(final Board board) {
            removeAll();
            for (TilePanel tilePanel : boardDirection.traverse(tileList)) {
                tilePanel.drawTile(board);
                this.add(tilePanel);
            }
            validate();
            repaint();
        }
    }
    public class TilePanel extends JPanel {
        final int tileID;
        public TilePanel(BoardPanel boardPanel, int tileID) {
            super(new GridBagLayout());
            this.tileID = tileID;
            this.setPreferredSize(TILE_PANEL_DIMENSION);
            this.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        //Right click means undo the piece(tile) you selected before
                        //clearTile();
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    }
                    else if (SwingUtilities.isLeftMouseButton(e)) {
                        //If you haven't select any piece to move
                        if (sourceTile == null) {
                            sourceTile = chessBoard.getTile(tileID);
                            humanMovedPiece = sourceTile.getPiece();
                            //If you left-click on a piece/tile that is empty
                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        }
                        else {
                            //Moving a piece
                            destinationTile = chessBoard.getTile(tileID);
                            final Move move = MoveFactory.createMove(chessBoard,sourceTile.getCoordinate(),destinationTile.getCoordinate());
                            final MoveTransition moveTransition = chessBoard.getCurrentPlayer().makeMove(move);
                            if (moveTransition.getMoveStatus().isDone()) {
                                chessBoard = moveTransition.getBoard();
                                moveLog.addMoves(move);
                                System.out.println(chessBoard);
                            }
                            clearTile();
                        }
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            gameHistoryPanel.redo(chessBoard,moveLog);
                            takenPiecesPanel.redo(moveLog);
                            boardPanel.drawBoard(chessBoard);
                        }
                    });
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            boardPanel.add(this);

            validate();
        }
        public void drawTile(Board board) {
            assignPieceIcon(board);
            assignTileColor();
            highlightLegalMoves(board);
            validate();
            repaint();
        }
        private void highlightLegalMoves(final Board board) {
            if (highlightLegal) {
                for (final Move move: pieceLegalMove(board)) {
                    if (move.getTargetCoordinate() == this.tileID) {
                        try {
                            this.add(new JLabel(new ImageIcon(ImageIO.read(new File("D:\\movesicon\\green_dot.png")))));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

        }
        private Collection<Move> pieceLegalMove(final Board board) {
            //TODO only get legal move to escape check when in check
//            if (board.getCurrentPlayer().isInCheck()) {
//                return board.getCurrentPlayer().getEscapeMoves();
//            }
            if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.getCurrentPlayer().getAlliance()) {
                return humanMovedPiece.getLegalMoves(board);
            }
            return Collections.emptyList();
        }
        //Rules implemented on the coloring of each tile
        private void assignTileColor() {
            if (BoardUtils.EIGHTH_RANK[tileID]
                    || BoardUtils.SIXTH_RANK[tileID]
                    || BoardUtils.FOURTH_RANK[tileID]
                    || BoardUtils.SECOND_RANK[tileID]) {
                this.setBackground(tileID % 2 == 0 ? lightColor : darkColor);
            }
            if (BoardUtils.SEVENTH_RANK[tileID]
                    || BoardUtils.FIFTH_RANK[tileID]
                    || BoardUtils.THIRD_RANK[tileID]
                    || BoardUtils.FIRST_RANK[tileID]) {
                this.setBackground(tileID % 2 == 0 ? darkColor : lightColor);
            }
        }
        //Assigning piece image for each piece based on a file of images
        private void assignPieceIcon(final Board board) {
            this.removeAll();
            if (board.getTile(this.tileID).isOccupied()) {
                try {
                    final Tile tile = board.getTile(this.tileID);
                    final BufferedImage imageIcon = ImageIO.read(new File(FILE_ICON_PATH
                            + tile.getPiece().getPieceAlliance().toString().substring(0,1)
                            + tile.getPiece().toString().substring(0,1)
                            + ".gif"));
                    final JLabel label = new JLabel(new ImageIcon(imageIcon));
                    this.add(label);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static class MoveLog {
        private final List<Move> moves;

        public MoveLog() {
            moves = new ArrayList<>();
        }
        public void addMoves(final Move move) {
            this.moves.add(move);
        }
        public List<Move> getMoves() {
            return this.moves;
        }
        public void clear() {
            moves.clear();
        }
        public int size() {
            return moves.size();
        }
        public Move removeMove(final int index) {
            return moves.remove(index);
        }
        public boolean removeMove(final Move move) {
            return moves.remove(move);
        }
    }
}
