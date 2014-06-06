package com.twsift.autotestmarker.decorators;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;

import com.twsift.autotestmarker.marker.MarkerHelper;

public class AutotestDecorator extends LabelProvider implements ILightweightLabelDecorator
{

	@Override
	public void decorate(Object resource, IDecoration decoration)
	{
		if( resource instanceof IResource )
		{
			if( MarkerHelper.findAutotestMarkers((IResource) resource, false).length > 0 )
			{
				decoration.addOverlay(
					ImageDescriptor.createFromFile(AutotestDecorator.class, "/icons/autotest_decorator.png"),
					IDecoration.TOP_LEFT);
			}
		}

	}

}
