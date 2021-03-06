package com.persesgames.jogl;

import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.opengl.GLWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.media.opengl.*;
import java.nio.FloatBuffer;

/**
 * Date: 10/25/13
 * Time: 7:42 PM
 */
public class Renderer implements GLEventListener  {
    private final static Logger logger = LoggerFactory.getLogger(Renderer.class);

    private volatile boolean stopped    = false;
    private volatile boolean dirty      = false;

    private ShaderProgram shaderProgram;

    private final GLWindow glWindow;

    private float[]                 vertices = {
            -0.5f, -0.5f, -0.5f,
             0.5f, -0.5f, 0.0f,
             0.0f,  0.5f, 0.5f,
    };

    private FloatBuffer             fbVertices          = Buffers.newDirectFloatBuffer(vertices);

    int[]                           vboHandles;
    private int                     vboVertices;

    private Keyboard                keyboard;

    public Renderer(GLWindow glWindow, Keyboard keyboard) {
        this.glWindow = glWindow;
        this.keyboard = keyboard;
    }

    public void stop() {
        stopped = true;
    }

    public void redraw() {
        dirty = true;
    }

    public void run() {
        Renderer.this.glWindow.display();

        while(!stopped) {
            if (dirty) {
                logger.info("rendering+" + System.currentTimeMillis());
                Renderer.this.glWindow.display();
                Renderer.this.glWindow.swapBuffers();
                dirty = false;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    logger.warn(e.getMessage(), e);
                }
            }

            stopped = keyboard.isPressed(KeyEvent.VK_ESCAPE);
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

        shaderProgram = new ShaderProgram(gl, Util.loadAsText(getClass(), "simpleShader.vert"), Util.loadAsText(getClass(), "simpleShader.frag"));

        vboHandles = new int[1];
        gl.glGenBuffers(1, vboHandles, 0);

        vboVertices = vboHandles[0];
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        logger.info("display+" + System.currentTimeMillis());

        GL2ES2 gl = drawable.getGL().getGL2ES2();

        // Clear screen
        gl.glClearColor(0.5f, 0, 0.5f, 1f);
        gl.glClear(GL2ES2.GL_COLOR_BUFFER_BIT);

        shaderProgram.begin();

        // Select the VBO, GPU memory data, to use for vertices
        gl.glBindBuffer(GL2ES2.GL_ARRAY_BUFFER, vboVertices);

        // transfer data to VBO, this perform the copy of data from CPU -> GPU memory
        int numBytes = vertices.length * 4;
        gl.glBufferData(GL.GL_ARRAY_BUFFER, numBytes, fbVertices, GL.GL_STATIC_DRAW);

        // Associate Vertex attribute 0 with the last bound VBO
        gl.glVertexAttribPointer(0 /* the vertex attribute */, 3,
                GL2ES2.GL_FLOAT, false /* normalized? */, 0 /* stride */,
                0 /* The bound VBO data offset */);

        gl.glEnableVertexAttribArray(0);

        gl.glDrawArrays(GL2ES2.GL_TRIANGLES, 0, 3); //Draw the vertices as triangle

        gl.glDisableVertexAttribArray(0); // Allow release of vertex position memory

        shaderProgram.end();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        logger.info("reshape+" + System.currentTimeMillis());

        GL2ES2 gl = drawable.getGL().getGL2ES2();

        gl.glViewport(0, 0, w, h);
    }

}
