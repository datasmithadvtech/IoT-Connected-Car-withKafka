/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ram.application.CORSfilter;

/**
 *
 * @author ram
 */
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

/**
 * Provide Cross Origin Request permissions for the dashboard to talk to the REST API.
 *
 * <em>Note:</em> This is not a secure approach and should not be used as openly as this
 * example.
 *
 * @author Michael Minella
 */
@Component
public class CORSfilter implements Filter {

        @Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader("Access-Control-Allow-Origin", "*");

		response.setHeader("Access-Control-Allow-Methods", "GET");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		chain.doFilter(req, res);
	}

        @Override
	public void init(FilterConfig filterConfig) {
	}

        @Override
	public void destroy() {
	}

}