package fatec.poo.control;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;

import fatec.poo.model.Produto;
import fatec.poo.model.Pedido;
import fatec.poo.model.Cliente;

public class DaoPedido {
    private Connection conn;
    private DaoCliente daoCliente;
    private DaoVendedor daoVendedor;
    private DaoItemPedido daoItemPedido;

    public DaoPedido(Connection conn) {
         this.conn = conn;
    }

    public void inserir(Pedido p) {
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("INSERT INTO Pedido (numero, dataEmissao, dataPagto, formaPagto, cliente, vendedor) VALUES(?,?,?,?,?,?)");
            ps.setString(1, p.getNumero());
            ps.setString(2, p.getDataEmissao());
            ps.setString(3, p.getDataPagto());
            
            if (p.getFormaPagto()) ps.setString(4, "P");
            else ps.setString(4, "V");
            
            ps.setString(5, p.getCliente().getCpf());
            ps.setString(6, p.getVendedor().getCpf());

            ps.execute();
        } catch (SQLException ex) {
             System.out.println(ex.toString());
        }
    }

    public void alterar(Produto p) {
        PreparedStatement ps = null;
        /*
        try {
            ps = conn.prepareStatement("UPDATE Produto SET "+
                "descricao = ?,"+
                "qtdeEstoque = ?,"+
                "unidadeMedida = ?,"+
                "preco = ?,"+
                "estoqueMinimo = ? WHERE codigo = ?"
            );

            ps.setString(1, p.getDescricao());
            ps.setDouble(2, p.getQtdeEstoque());
            ps.setString(3, p.getUnidadeMedida());
            ps.setDouble(4, p.getPreco());
            ps.setDouble(5, p.getEstoqueMinimo());
            ps.setString(6, p.getCodigo());

            ps.execute();
        } catch (SQLException ex) {
             System.out.println(ex.toString());
        }
        */
    }

    public Pedido consultar (String numero) {
        Pedido p = null;

        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM Pedido WHERE numero = ?");

            ps.setString(1, numero);
            ResultSet rs = ps.executeQuery();

            if (rs.next() == true) {
                p = new Pedido (numero, rs.getString("dataEmissao"));
                p.setDataPagto(rs.getString("dataPagto"));
                if ("P".equals(rs.getString("formaPagto"))) p.setFormaPagto(true);
                else p.setFormaPagto(false);

                daoCliente = new DaoCliente(conn);
                p.setCliente(daoCliente.consultar(rs.getString("cliente")));
                
                daoVendedor = new DaoVendedor(conn);
                p.setVendedor(daoVendedor.consultar(rs.getString("vendedor")));
                
                daoItemPedido = new DaoItemPedido(conn);
                p.setItens(daoItemPedido.buscarItens(numero, p));
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.toString());
        }
        return p;
    }

    public void excluir(Pedido p) {
        PreparedStatement ps = null;
        /* TODO: antes de excluir o pedido, é preciso excluir todos os itens
           referentes a ele na tabela itemPedido
        try {
            ps = conn.prepareStatement("DELETE FROM Produto WHERE codigo = ?");
            ps.setString(1, p.getCodigo());
            ps.execute();
        } catch (SQLException ex) {
             System.out.println(ex.toString());
        }
        */
    }
}
