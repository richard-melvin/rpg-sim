/* ===================
 * Orson Charts - Demo
 * ===================
 *
 * Copyright 2013-2022, by David Gilbert. All rights reserved.
 *
 * https://github.com/jfree/jfree-demos
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   - Neither the name of the JFree organisation nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL OBJECT REFINERY LIMITED BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Note that the above terms apply to the demo source only, and not the 
 * Orson Charts library.
 * 
 */

package dsim.test;

import java.awt.Dimension;
import java.nio.file.Path;

import org.jfree.chart3d.Chart3D;
import org.jfree.chart3d.Chart3DFactory;
import org.jfree.chart3d.Orientation;
import org.jfree.chart3d.axis.ValueAxis3D;
import org.jfree.chart3d.data.Range;
import org.jfree.chart3d.data.function.Function3D;
import org.jfree.chart3d.export.ExportUtils;
import org.jfree.chart3d.graphics3d.Dimension3D;
import org.jfree.chart3d.legend.LegendAnchor;
import org.jfree.chart3d.plot.XYZPlot;
import org.jfree.chart3d.renderer.RainbowScale;
import org.jfree.chart3d.renderer.xyz.SurfaceRenderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * A demo of a surface chart.
 */
public class GraphPlotterTest  {


    /**
     * Creates a surface chart for the demo.
     * 
     * @return A surface chart.
     */
    private static Chart3D createChart() {
        Function3D function = (x, z) -> Math.sin(x * x + z * z);
        
        Chart3D chart = Chart3DFactory.createSurfaceChart(
                "SurfaceRendererDemo2", 
                "y = sin(x^2 + z^2)", 
                function, "X", "Y", "Z");
        XYZPlot plot = (XYZPlot) chart.getPlot();
        plot.setDimensions(new Dimension3D(10, 5, 10));
        ValueAxis3D xAxis = plot.getXAxis();
        xAxis.setRange(-2, 2);
        ValueAxis3D zAxis = plot.getZAxis();
        zAxis.setRange(-2, 2);
        SurfaceRenderer renderer = (SurfaceRenderer) plot.getRenderer();
        renderer.setColorScale(new RainbowScale(new Range(-1.0, 1.0)));
        renderer.setDrawFaceOutlines(false);
        chart.setLegendPosition(LegendAnchor.BOTTOM_RIGHT, 
                Orientation.VERTICAL);
        return chart;
    }

        
    /**
     * Starting point for the app.
     *
     * @param args  command line arguments (ignored).
     */
    @Test
    public  void sampleDataPlot(TestInfo info) {
    	Chart3D chart = createChart();
        
        ExportUtils.writeAsSVG(chart, 2048,1024, Path.of("./target").resolve(testNameFile(info)).toFile());
    }


	private String testNameFile(TestInfo info) {
		return info.getTestMethod().get().getName() + ".svg";
	}
}


