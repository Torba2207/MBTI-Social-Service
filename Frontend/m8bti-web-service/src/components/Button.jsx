import { forwardRef } from 'react'
import Link from 'next/link'
import clsx from 'clsx'
import { MBTIColors } from './MBTIColors'

const baseStyles = {
  solid:
    'inline-flex justify-center rounded-lg py-2 px-3 text-sm font-semibold transition-colors',
  outline:
    'inline-flex justify-center rounded-lg border py-[calc(theme(spacing.2)-1px)] px-[calc(theme(spacing.3)-1px)] text-sm outline-2 outline-offset-2 transition-colors',
}

const variantStyles = {
  solid: {
    none:'',
    purple:'bg-[#785D87] text-[#F4EDF6] active:bg-[#482D57]', 
    cyan: 'relative overflow-hidden bg-cyan-500 text-white before:absolute before:inset-0 active:before:bg-transparent hover:before:bg-white/10 active:bg-cyan-600 active:text-white/80 before:transition-colors',
    white:
      'bg-white text-cyan-900 hover:bg-white/90 active:bg-white/90 active:text-cyan-900/70',
    gray: 'bg-gray-800 text-white hover:bg-gray-900 active:bg-gray-800 active:text-white/80',
    blue:"inline-block px-6 py-2.5 bg-sky-700 text-white font-medium text-xs leading-tight uppercase rounded shadow-md hover:bg-sky-800 hover:shadow-lg focus:bg-blue-700 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-blue-800 active:shadow-lg transition duration-150 ease-in-out"
  },
  outline: {
    gray: 'border-gray-300 text-gray-700 hover:border-gray-400 active:bg-gray-100 active:text-gray-700/80',
  },
}


export const Button = forwardRef(function Button(
  { 
    variant = 'solid', 
    color = 'gray', 
    className, 
    href, 
    isDynamic = false,
    dynamicStyle = {}, // { backgroundColor, color, etc. }
    ...props 
  },
  ref
) {
  // Base class names
  const baseClassName = clsx(
    !isDynamic && baseStyles[variant], // Only apply static styles if not dynamic
    !isDynamic && variantStyles[variant][color], // Only apply static color if not dynamic
    className
  );

  // Merge dynamic styles with any additional styles passed via props
  const style = isDynamic ? { 
    ...dynamicStyle,
    transition: 'all 1s ease-in-out', // Add smooth transition for dynamic changes
    ...props.style 
  } : props.style;

  return href ? (
    <Link 
      ref={ref} 
      href={href} 
      className={baseClassName}
      style={style}
      {...props} 
    />
  ) : (
    <button 
      ref={ref} 
      className={baseClassName}
      style={style}
      {...props} 
    />
  );
});