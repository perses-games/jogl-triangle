package com.persesgames.jogl;

import com.jogamp.graph.curve.opengl.TextRenderer;
import com.jogamp.newt.opengl.GLWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.*;

/**
 * Date: 10/25/13
 * Time: 7:42 PM
 */
public class Renderer implements GLEventListener  {
    private final static Logger logger = LoggerFactory.getLogger(Renderer.class);

    private volatile boolean stopped    = false;
    private volatile boolean dirty      = false;

    private TextRenderer textRenderer;

    private final GLWindow glWindow;

    public Renderer(GLWindow glWindow) {
        this.glWindow = glWindow;
    }

    public void stop() {
        stopped = true;
    }

    public void redraw() {
        dirty = true;
    }

    public void run() {
        while(!stopped) {
            if (dirty) {
                logger.info("rendering+" + System.currentTimeMillis());
                Renderer.this.glWindow.display();
                dirty = false;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }

        Renderer.this.glWindow.destroy();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2ES2 gl = drawable.getGL().getGL2ES2();

        logger.info("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
        logger.info("INIT GL IS: " + gl.getClass().getName());
        logger.info("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
        logger.info("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
        logger.info("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));

        int [] result = new int[1];
        gl.glGetIntegerv(GL2.GL_MAX_VERTEX_ATTRIBS, result, 0);
        logger.info("GL_MAX_VERTEX_ATTRIBS=" + result[0]);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        logger.info("display+" + System.currentTimeMillis());

        GL2ES2 gl = drawable.getGL().getGL2ES2();

        drawable.swapBuffers();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        logger.info("reshape+" + System.currentTimeMillis());

        GL2ES2 gl = drawable.getGL().getGL2ES2();

        gl.glViewport(0, 0, w, h);
    }

}
