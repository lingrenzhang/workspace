package com.shs.liangdiaosi;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CheckCode
 */
public class CheckCode extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckCode() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BufferedImage img = new BufferedImage(68,22,BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		Random r = new Random();
		Color c = new Color(200,150,255);
		g.setColor(c);
		g.fillRect(0,0,68,22);
		StringBuffer sb = new StringBuffer();
		char[] ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		int index, len = ch.length;
		for (int i=0;i<4;i++){
			index = r.nextInt(len);
			g.setColor(new Color(r.nextInt(88),r.nextInt(188),r.nextInt(255)));
			g.setFont(new Font("Arial",Font.BOLD | Font.ITALIC,22));
			g.drawString("" + ch[index], (i*15)+3, 18);
			sb.append(ch[index]);
		}
		request.getSession().setAttribute("piccode",sb.toString());
		ImageIO.write(img, "JPG", response.getOutputStream());
	}

}
