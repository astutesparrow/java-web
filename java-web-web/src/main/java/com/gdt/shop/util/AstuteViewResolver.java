package com.gdt.shop.util;

import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;
import org.springframework.web.servlet.view.velocity.VelocityView;

public class AstuteViewResolver extends AbstractTemplateViewResolver {

	private String dateToolAttribute;

	private String numberToolAttribute;

	private String toolboxConfigLocation;

	public AstuteViewResolver() {
		setViewClass(requiredViewClass());
	}

	/**
	 * Requires {@link VelocityView}.
	 */
	@Override
	protected Class<?> requiredViewClass() {
		return VelocityView.class;
	}

	/**
	 * Set the name of the DateTool helper object to expose in the Velocity
	 * context of this view, or <code>null</code> if not needed. DateTool is
	 * part of Velocity Tools 1.0.
	 * 
	 * @see org.apache.velocity.tools.generic.DateTool
	 * @see VelocityView#setDateToolAttribute
	 */
	public void setDateToolAttribute(String dateToolAttribute) {
		this.dateToolAttribute = dateToolAttribute;
	}

	/**
	 * Set the name of the NumberTool helper object to expose in the Velocity
	 * context of this view, or <code>null</code> if not needed. NumberTool is
	 * part of Velocity Tools 1.1.
	 * 
	 * @see org.apache.velocity.tools.generic.NumberTool
	 * @see VelocityView#setNumberToolAttribute
	 */
	public void setNumberToolAttribute(String numberToolAttribute) {
		this.numberToolAttribute = numberToolAttribute;
	}

	/**
	 * Set a Velocity Toolbox config location, for example
	 * "/WEB-INF/toolbox.xml", to automatically load a Velocity Tools toolbox
	 * definition file and expose all defined tools in the specified scopes. If
	 * no config location is specified, no toolbox will be loaded and exposed.
	 * <p>
	 * The specfied location string needs to refer to a ServletContext resource,
	 * as expected by ServletToolboxManager which is part of the view package of
	 * Velocity Tools.
	 * <p>
	 * <b>Note:</b> Specifying a toolbox config location will lead to
	 * VelocityToolboxView instances being created.
	 * 
	 * @see org.apache.velocity.tools.view.servlet.ServletToolboxManager#getInstance
	 * @see VelocityToolboxView#setToolboxConfigLocation
	 */
	public void setToolboxConfigLocation(String toolboxConfigLocation) {
		this.toolboxConfigLocation = toolboxConfigLocation;
	}

	@Override
	protected void initApplicationContext() {
		super.initApplicationContext();

		if (this.toolboxConfigLocation != null) {
			if (VelocityView.class.equals(getViewClass())) {
				logger.info("Using VelocityToolboxView instead of default VelocityView "
						+ "due to specified toolboxConfigLocation");
				setViewClass(AstuteToolboxView.class);
			} else if (!AstuteToolboxView.class
					.isAssignableFrom(getViewClass())) {
				throw new IllegalArgumentException(
						"Given view class ["
								+ getViewClass().getName()
								+ "] is not of type ["
								+ AstuteToolboxView.class.getName()
								+ "], which it needs to be in case of a specified toolboxConfigLocation");
			}
		}
	}

	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		VelocityView view = (VelocityView) super.buildView(viewName);
		view.setDateToolAttribute(this.dateToolAttribute);
		view.setNumberToolAttribute(this.numberToolAttribute);
		if (this.toolboxConfigLocation != null) {
			((AstuteToolboxView) view)
					.setToolboxConfigLocation(this.toolboxConfigLocation);
		}
		return view;
	}

}
