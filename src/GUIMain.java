import recursos.Constantes;
import servico.LocadoraService;
import visao.PrincipalView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static recursos.ConfigConstantes.*;
import static recursos.Constantes.EXCECAO;

public class GUIMain {
    private static Logger logger = Logger.getLogger(GUIMain.class.getName());

    public static void main(String[] args) throws Exception {
        Connection conn = null;
        try {
            Class.forName(DB_TYPE);
            conn = DriverManager.getConnection(URL_DB, USER_DB, SENHA_DB);
            conn.setAutoCommit(false);

            //Demonstrar o funcionamento aqui
            LocadoraService.getInstance().setConnection(conn);
            PrincipalView principalView = new PrincipalView();
            principalView.renderizar();

        } catch (Exception e) {
            logger.log(SEVERE, EXCECAO, e);
        }
        logger.log(INFO, Constantes.FIM_TESTE);


    }
}
