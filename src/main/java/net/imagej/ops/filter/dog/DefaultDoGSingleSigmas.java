/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.ops.filter.dog;

import java.util.Arrays;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops.Filter.DoG;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.dog.DifferenceOfGaussian;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.thread.ThreadService;

/**
 * Implementation of {@link DifferenceOfGaussian} where all sigmas are the same
 * in each dimension. Internally running {@link DifferenceOfGaussian} on arrays.
 * 
 * @author Christian Dietz (University of Konstanz)
 * @param <T>
 */
@Plugin(type = DoG.class, name = DoG.NAME, priority = 1.0)
public class DefaultDoGSingleSigmas<T extends NumericType<T> & NativeType<T>>
	extends
	AbstractOutputFunction<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
	implements DoG
{

	@Parameter
	private ThreadService ts;

	@Parameter
	private OpService ops;

	@Parameter
	private double sigma1;

	@Parameter
	private double sigma2;

	@Parameter(required = false)
	private OutOfBoundsFactory<T, RandomAccessibleInterval<T>> fac;

	@SuppressWarnings("unchecked")
	@Override
	public RandomAccessibleInterval<T> createOutput(
		final RandomAccessibleInterval<T> input)
	{
		return (RandomAccessibleInterval<T>) ops.create().img(input);
	}

	@Override
	protected void safeCompute(final RandomAccessibleInterval<T> input,
		final RandomAccessibleInterval<T> output)
	{
		final double[] sigmas1 = new double[input.numDimensions()];
		final double[] sigmas2 = new double[input.numDimensions()];

		Arrays.fill(sigmas1, sigma1);
		Arrays.fill(sigmas2, sigma2);

		ops.run(DoG.class, output, input, sigmas1, sigmas2, fac);
	}
}
