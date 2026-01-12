package es.altia.agora.interfaces.user.web.escritorio;

import es.altia.common.service.config.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import org.apache.struts.action.*;

public final class LogoutAction extends Action {

    protected static Logger mlog =
            LogManager.getLogger(LogoutAction.class.getName());
    protected static Config m_Conf = ConfigServiceHelper.getConfig("common");

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        mlog.debug("LogoutAction--------->BEGIN");
        HttpSession session = request.getSession();
        //Invalidamos la sesion
        session.invalidate();
        mlog.debug("LogoutAction--------->END");
        return null;
    }
}

