package com.gibbon.etc.apt.generate;

import com.gibbon.etc.apt.annotation.DownloadMoreListener;

import java.util.Iterator;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * @author zhipeng.zhuo
 * @date 2020-03-13
 */
public class DownLoadMoreGenerator implements IGenerator {

    @Override
    public void generateMethod(List<Element> elements, TypeElement typeElement, StringBuilder builder) {

        builder.append("public void initDownLoadMore(" + typeElement.getQualifiedName() + " host, Object source ) {\n");
        builder.append("RecyclerView view;\n");
        Iterator<Element> iterator = elements.iterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            DownloadMoreListener annotation = element.getAnnotation(DownloadMoreListener.class);
            if (annotation != null) {
                ExecutableElement variableElement = (ExecutableElement) element;
                generate(variableElement, builder);
                iterator.remove();
            }
        }
        builder.append("}");


    }

    @Override
    public void generate(Element executableElement, StringBuilder builder) {
        int id = executableElement.getAnnotation(DownloadMoreListener.class).value();
        final int loadPosition = executableElement.getAnnotation(DownloadMoreListener.class).loadPosition();

        String method = executableElement.getSimpleName().toString();

        builder.append(" if(source instanceof android.app.Activity){\n");

        if (id == -1) {
            builder.append("view=((RecyclerView)(((android.app.Activity)source).findViewById( " + "R.id." + method + ")));\n");
        } else {
            builder.append("view=((RecyclerView)(((android.app.Activity)source).findViewById( " + id + ")));\n");
        }

        builder.append("\n}else{\n");
        if (id == -1) {
            builder.append("view=((RecyclerView)(((android.view.View)source).findViewById( " + "R.id." + method + ")));\n");
        } else {
            builder.append("view=((RecyclerView)(((android.view.View)source).findViewById( " + id + ")));\n");
        }
        builder.append("  }\n");

        builder.append("view.addOnScrollListener(new RecyclerView.OnScrollListener() {\n" +
                "            int layoutManagerType;\n" +
                "            int lastVisibleItemPosition;\n" +
                "            int[] lastPositions;\n" +
                "\n" +
                "            @Override\n" +
                "            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {\n" +
                "                super.onScrollStateChanged(recyclerView, newState);\n" +
                "\n" +
                "                if (newState == RecyclerView.SCROLL_STATE_IDLE) {\n" +
                "                    int last = lastVisibleItemPosition;\n" +
                "                    if (last + " + loadPosition +  " >= recyclerView.getAdapter().getItemCount()) {\n" +
                "                       host." + method + "();\n" +
                "                    }\n" +
                "                }\n" +
                "\n" +
                "            }\n" +
                "\n" +
                "            @Override\n" +
                "            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {\n" +
                "                super.onScrolled(recyclerView, dx, dy);\n" +
                "\n" +
                "                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();\n" +
                "                if (layoutManager instanceof LinearLayoutManager) {\n" +
                "                    layoutManagerType = 0;\n" +
                "                } else if (layoutManager instanceof StaggeredGridLayoutManager) {\n" +
                "                    layoutManagerType = 1;\n" +
                "                } else {\n" +
                "                    throw new RuntimeException(\n" +
                "                            \"Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager\");\n" +
                "                }\n" +
                "\n" +
                "                switch (layoutManagerType) {\n" +
                "                    case 0:\n" +
                "                        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();\n" +
                "                        break;\n" +
                "                    case 2:\n" +
                "                        lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();\n" +
                "                        break;\n" +
                "                    case 1:\n" +
                "                        StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;\n" +
                "                        if (lastPositions == null) {\n" +
                "                            lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];\n" +
                "                        }\n" +
                "                        staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);\n" +
                "\n" +
                "                        int max = lastPositions[0];\n" +
                "                        for (int value : lastPositions) {\n" +
                "                            if (value > max) {\n" +
                "                                max = value;\n" +
                "                            }\n" +
                "                        }\n" +
                "                        lastVisibleItemPosition = max;\n" +
                "                        break;\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "        });");
    }

    @Override
    public void generateInitMethod(StringBuilder builder) {
        builder.append("initDownLoadMore(host,source);\n");
    }

    @Override
    public void generateImport(StringBuilder builder) {
        builder.append("import com.gibbon.etc.apt.annotation.DownloadMoreListener;\n");
        builder.append("import androidx.recyclerview.widget.RecyclerView;\n");
        builder.append("import androidx.recyclerview.widget.GridLayoutManager;\n");
        builder.append("import androidx.recyclerview.widget.LinearLayoutManager;\n");
        builder.append("import androidx.recyclerview.widget.StaggeredGridLayoutManager;\n");
    }
}
