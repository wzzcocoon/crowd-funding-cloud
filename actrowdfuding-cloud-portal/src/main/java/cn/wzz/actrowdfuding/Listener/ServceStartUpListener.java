package cn.wzz.actrowdfuding.Listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
	@WebListener表示这是一个监听器
	@WebFilter表示这是一个过滤器
	@WebServlet表示这是一个Servlet
 */
@WebListener
public class ServceStartUpListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		ServletContext application = sce.getServletContext();
		application.setAttribute("APP_PATH", application.getContextPath());
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}


}
