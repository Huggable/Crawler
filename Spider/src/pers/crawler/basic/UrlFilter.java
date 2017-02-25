package pers.crawler.basic;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by LY on 2/15/2017.
 */
public class UrlFilter {
    private Set<Object> urls;
    public UrlFilter()
    {
        urls = Collections.synchronizedSet(new LinkedHashSet<>());
    }
    public  synchronized boolean contains(Object o)
    {
        return urls.contains(o);
    }
    public  synchronized void add(Object o)
    {
        urls.add(o);
    }
    public  synchronized Object getNext()
    {
        Iterator<Object> it = urls.iterator();
        if(it.hasNext())
        {
            Object o = it.next();
            it.remove();
            return o;
        }
        else return null;
    }

    public  int size()
    {
        return urls.size();
    }
    public boolean isEmpty()
    {
        return urls.isEmpty();
    }

}
