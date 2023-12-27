package gui;

import engine.board.Alliance;
import engine.board.Board;
import engine.board.Move;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
/*
Panel for showing the history of the game
WHITE - BLACK
QA8     BB8
etc...
 */

public class GameHistoryPanel extends JPanel {

    private final DataModel dataModel;
    private final JScrollPane scrollPane;
    private final Dimension HISTORY_PANEL_DIMENSION = new Dimension(125,600);

    public GameHistoryPanel() {
        this.setLayout(new BorderLayout());

        this.dataModel = new DataModel();
        final JTable table = new JTable(dataModel);
        table.setRowHeight(15);

        this.scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(HISTORY_PANEL_DIMENSION);

        this.add(scrollPane,BorderLayout.CENTER);
        this.setVisible(true);
    }
    void redo(final Board board, final Table.MoveLog moveHistory) {
        int currentRow = 0;
        this.dataModel.clear();
        for (final Move move: moveHistory.getMoves()) {
            if (move == null) {
                break;
            }
            final String moveText = move.toString();
            if (move.getMovedPiece().getPieceAlliance() == Alliance.WHITE) {
                this.dataModel.setValueAt(moveText,currentRow,0);
            }
            else if (move.getMovedPiece().getPieceAlliance() == Alliance.BLACK) {
                this.dataModel.setValueAt(moveText,currentRow,1);
                currentRow++;
            }
        }
        if (moveHistory.getMoves().size() > 0) {
            final Move lastMove = moveHistory.getMoves().get(moveHistory.getMoves().size() - 1);
            if (lastMove != null) {
                final String lastMoveText = lastMove.toString();
                if (lastMove.getMovedPiece().getPieceAlliance() == Alliance.WHITE) {
                    this.dataModel.setValueAt(lastMoveText + calculateCheckmateAndCheckHash(board),currentRow,0);
                }
                else if (lastMove.getMovedPiece().getPieceAlliance() == Alliance.BLACK) {
                    this.dataModel.setValueAt(lastMoveText + calculateCheckmateAndCheckHash(board),currentRow - 1,1);
                }
            }
        }
        final JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setValue(scrollBar.getMaximum());
    }

    private String calculateCheckmateAndCheckHash(Board board) {
        if (board.getCurrentPlayer().isInCheckMate()) {
            return "#";
        }
        else if (board.getCurrentPlayer().isInCheck()) {
            return "+";
        }
        return "";
    }

    public static class DataModel extends DefaultTableModel {
        final List<Row> rowList;
        private final String[] NAMES = {"White","Black"};
        public DataModel() {
            rowList = new ArrayList<>();
        }
        public void clear() {
            rowList.clear();
            setRowCount(0);
        }
        @Override
        public int getRowCount() {
            if (this.rowList == null) {
                return 0;
            }
            return rowList.size();
        }
        @Override
        public int getColumnCount() {
            return NAMES.length;
        }
        @Override
        public Object getValueAt(final int row, final int column) {
            final Row currentRow = this.rowList.get(row);
            if(column == 0) {
                return currentRow.getWhiteMove();
            } else if (column == 1) {
                return currentRow.getBlackMove();
            }
            return null;
        }
        @Override
        public void setValueAt(final Object aValue, final int row, final int column) {
            final Row tempRow;
            if (row >= rowList.size()) {
                tempRow = new Row();
                this.rowList.add(tempRow);
            }
            else {
                tempRow = this.rowList.get(row);
            }
            if (column == 0) {
                tempRow.setWhiteMove((String) aValue);
                fireTableRowsInserted(row,row);
            }
            else if (column == 1) {
                tempRow.setBlackMove((String) aValue);
                fireTableCellUpdated(row,column);
            }
        }

        @Override
        public Class<?> getColumnClass(int index) {
            return Move.class;
        }

        @Override
        public String getColumnName(int column) {
            if (column == 0 || column == 1) {
                return NAMES[column];
            }
            return null;
        }
    }
    private static class Row {
        private String whiteMove;
        private String blackMove;

        public Row() {
        }

        public String getWhiteMove() {
            return whiteMove;
        }

        public void setWhiteMove(final String move) {
            this.whiteMove = move;
        }

        public String getBlackMove() {
            return blackMove;
        }

        public void setBlackMove(final String move) {
            this.blackMove = move;
        }
    }
}
