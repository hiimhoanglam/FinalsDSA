package gui;
import engine.board.*;
import engine.piece.Piece;
import engine.board.MoveTransition;
import engine.player.ai.AlphaBeta;
import engine.player.ai.Minimax;
import engine.player.ai.MoveStrategy;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Table  {
    private final PropertyChangeSupport support;
    private static volatile Table instance;
    JFrame gameFrame;
    private final GameHistoryPanel gameHistoryPanel;
    private final TakenPiecesPanel takenPiecesPanel;
    private final GameSetup gameSetup;

    private static final Dimension OUTER_FRAME = new Dimension(600,600);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(500,500);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    private static final String FILE_ICON_PATH = "D:\\chesspieces\\";
    private static final Color lightColor = new Color(177, 228, 185);
    private static final Color darkColor = new Color(112, 162, 163);
    private BoardDirection boardDirection;
    private Board gameBoard;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private final MoveLog moveLog;
    private final BoardPanel boardPanel;
    private boolean highlightLegal;
    private Move computerMove;

    private Table() {
        this.gameBoard = Board.initBoard();
//        this.chessBoard = Board.initBoard("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1");
        this.boardDirection = BoardDirection.NORMAL;
        this.boardPanel = new BoardPanel();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.gameSetup = new GameSetup(this.gameFrame,true);
        this.moveLog = new MoveLog();
        this.highlightLegal = false;

        this.support = new PropertyChangeSupport(this);
        this.support.addPropertyChangeListener(new TableGameAIWatcher());

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
    public static Table getTable() {
        Table result = instance;
        if (result != null) {
            return result;
        }
        synchronized(Table.class) {
            if (instance == null) {
                instance = new Table();
            }
            return instance;
        }
    }

    private JFrame getGameFrame() {
        return gameFrame;
    }

    private GameHistoryPanel getGameHistoryPanel() {
        return gameHistoryPanel;
    }

    private TakenPiecesPanel getTakenPiecesPanel() {
        return takenPiecesPanel;
    }

    private GameSetup getGameSetup() {
        return gameSetup;
    }

    private Board getGameBoard() {
        return gameBoard;
    }

    private MoveLog getMoveLog() {
        return moveLog;
    }

    private BoardPanel getBoardPanel() {
        return boardPanel;
    }

    private boolean isHighlightLegal() {
        return highlightLegal;
    }

    public void show() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Table.getTable().getMoveLog().clear();
                Table.getTable().getGameHistoryPanel().redo(gameBoard, Table.getTable().getMoveLog());
                Table.getTable().getTakenPiecesPanel().redo(Table.getTable().getMoveLog());
                Table.getTable().getBoardPanel().drawBoard(Table.getTable().getGameBoard());
            }
        });
    }
    private void clearTile() {
        sourceTile = null;
        destinationTile = null;
        humanMovedPiece = null;
    }
    private void populateMenuBar(JMenuBar menubar) {
        menubar.add(createFileMenu());
        menubar.add(createPreferencesMenu());
        menubar.add(createOptionsMenu());
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
                boardPanel.drawBoard(gameBoard);
            }
        });
        preferences.add(flip);
        preferences.addSeparator();
        final JCheckBoxMenuItem highlightCheckBox = new JCheckBoxMenuItem("Highlight legal moves",false);
        highlightCheckBox.addActionListener(e -> highlightLegal = highlightCheckBox.isSelected());
        preferences.add(highlightCheckBox);
        return preferences;
    }
    private JMenu createOptionsMenu() {
        final JMenu optionsMenu = new JMenu("Options");
        optionsMenu.setMnemonic(KeyEvent.VK_0);
        final JMenuItem setupGameMenuItem = new JMenuItem("Setup Game", KeyEvent.VK_S);
        setupGameMenuItem.addActionListener(e -> {
            Table.getTable().getGameSetup().promptUser();
            Table.getTable().setupUpdate(Table.getTable().getGameSetup());
        });
        final JMenuItem undoMoveMenuItem = new JMenuItem("Undo last move", KeyEvent.VK_M);
        undoMoveMenuItem.addActionListener(e -> {
            if(Table.getTable().getMoveLog().size() > 0) {
                undoLastMove();
            }
        });
        final JMenuItem resetMenuItem = new JMenuItem("New Game", KeyEvent.VK_P);
        resetMenuItem.addActionListener(e -> undoAllMoves());
        optionsMenu.add(resetMenuItem);
        optionsMenu.add(undoMoveMenuItem);
        optionsMenu.add(setupGameMenuItem);
        return optionsMenu;
    }

    private void undoAllMoves() {
        for(int i = Table.getTable().getMoveLog().size() - 1; i >= 0; i--) {
            final Move lastMove = Table.getTable().getMoveLog().removeMove(Table.getTable().getMoveLog().size() - 1);
            this.gameBoard = this.gameBoard.getCurrentPlayer().unMakeMove(lastMove).getBoard();
        }
        this.computerMove = null;
        Table.getTable().getMoveLog().clear();
        Table.getTable().getGameHistoryPanel().redo(gameBoard, Table.getTable().getMoveLog());
        Table.getTable().getTakenPiecesPanel().redo(Table.getTable().getMoveLog());
        Table.getTable().getBoardPanel().drawBoard(gameBoard);
    }

    private void undoLastMove() {
        final Move lastMove = Table.getTable().getMoveLog().removeMove(Table.getTable().getMoveLog().size() - 1);
        this.gameBoard = this.gameBoard.getCurrentPlayer().unMakeMove(lastMove).getBoard();
        this.computerMove = null;
        Table.getTable().getMoveLog().removeMove(lastMove);
        Table.getTable().getGameHistoryPanel().redo(getGameBoard(), Table.getTable().getMoveLog());
        Table.getTable().getTakenPiecesPanel().redo(Table.getTable().getMoveLog());
        Table.getTable().getBoardPanel().drawBoard(gameBoard);
    }

    private void setupUpdate(final GameSetup gameSetup) {
        support.firePropertyChange("GameSetup update",Table.getTable().getGameSetup(),gameSetup);
    }
    private void moveMadeUpdate(PlayerType playerType) {
        support.firePropertyChange("PlayerType update",Table.getTable().getGameSetup(),playerType);
    }
    private static class TableGameAIWatcher implements PropertyChangeListener{
        private GameSetup gameSetup;
        private PlayerType playerType;
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (Table.getTable().getGameSetup().isAIPlayer(Table.getTable().getGameBoard().getCurrentPlayer())
            && !Table.getTable().getGameBoard().getCurrentPlayer().isInCheckMate()
            && !Table.getTable().getGameBoard().getCurrentPlayer().isInStaleMate()) {
                final AIThinkTank aiThinkTank = new AIThinkTank();
                aiThinkTank.execute();
            }
            if (Table.getTable().getGameBoard().getCurrentPlayer().isInCheckMate()) {
                JOptionPane.showMessageDialog(Table.getTable().getBoardPanel(),
                        "Game Over: Player " + Table.getTable().getGameBoard().getCurrentPlayer() + " is in checkmate!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            if (Table.getTable().getGameBoard().getCurrentPlayer().isInStaleMate()) {
                JOptionPane.showMessageDialog(Table.getTable().getBoardPanel(),
                        "Game Over: Player " + Table.getTable().getGameBoard().getCurrentPlayer() + " is in stalemate!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            if (Table.getTable().getMoveLog().isDrawByRepetition()) {
                JOptionPane.showMessageDialog(Table.getTable().getBoardPanel(),
                        "Game Over: Draw by repetition ", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }

        public GameSetup getGameSetup() {
            return gameSetup;
        }

        public void setGameSetup(GameSetup gameSetup) {
            this.gameSetup = gameSetup;
        }

        public PlayerType getPlayerType() {
            return playerType;
        }

        public void setPlayerType(PlayerType playerType) {
            this.playerType = playerType;
        }
    }
    private static class AIThinkTank extends SwingWorker<Move,String> {

        @Override
        protected Move doInBackground() {
            //TODO change this to Alpha-Beta pruning = new AlphaBeta(...)
            final MoveStrategy strategy = new AlphaBeta(Table.getTable().getGameSetup().getSearchDepth());
            return strategy.execute(Table.getTable().getGameBoard());
        }

        @Override
        protected void done() {
            //After the AI analyzed and return the move, redo the GUI
            try {
                final Move bestMove = get();
                Table.getTable().getMoveLog().addMoves(bestMove);
                Table.getTable().updateComputerMove(bestMove);
                Table.getTable().updateGameBoard(Table.getTable().getGameBoard().getCurrentPlayer().makeMove(bestMove).getBoard());
                Table.getTable().getGameHistoryPanel().redo(Table.getTable().getGameBoard(),Table.getTable().getMoveLog());
                Table.getTable().getTakenPiecesPanel().redo(Table.getTable().getMoveLog());
                Table.getTable().getBoardPanel().drawBoard(Table.getTable().getGameBoard());
                Table.getTable().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void updateGameBoard(final Board board) {
        this.gameBoard = board;
    }

    private void updateComputerMove(Move computerMove) {
        this.computerMove = computerMove;
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
    public enum PlayerType {
        HUMAN,
        COMPUTER
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
                tilePanel.assignPieceIcon(gameBoard);
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
                            sourceTile = gameBoard.getTile(tileID);
                            humanMovedPiece = sourceTile.getPiece();
                            //If you left-click on a piece/tile that is empty
                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        }
                        else {
                            //Moving a piece
                            destinationTile = gameBoard.getTile(tileID);
                            final Move move = MoveFactory.createMove(gameBoard,sourceTile.getCoordinate(),destinationTile.getCoordinate());
                            final MoveTransition moveTransition = gameBoard.getCurrentPlayer().makeMove(move);
                            if (moveTransition.getMoveStatus().isDone()) {
                                gameBoard = moveTransition.getBoard();
                                moveLog.addMoves(move);
                            }
                            clearTile();
                        }
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            gameHistoryPanel.redo(gameBoard,moveLog);
                            takenPiecesPanel.redo(moveLog);
                            if (gameSetup.isAIPlayer(gameBoard.getCurrentPlayer())) {
                                moveMadeUpdate(PlayerType.HUMAN);
                            }
                            boardPanel.drawBoard(gameBoard);
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
            if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.getCurrentPlayer().getAlliance()) {
                if (humanMovedPiece.isKing()) {
                    final Collection<Move> kingLegalMoves = humanMovedPiece.getLegalMoves(board);
                    kingLegalMoves.addAll(board.getCurrentPlayer().calculateKingCastle(board.getCurrentPlayer().getLegalMoves()
                        ,board.getCurrentPlayer().getOpponent().getLegalMoves()));
                    return kingLegalMoves;
                }
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
        /*
        Imagine move log is like this
        a1 a2
        b1 b2
        c1 c2
        a1 a2
        b1 b2
        c1 c2
         */
        public boolean isDrawByRepetition() {
            //TODO draw by repetition
            return false;
        }
    }
}
