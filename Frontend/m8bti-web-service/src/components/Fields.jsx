import clsx from 'clsx'

const formClasses =
  'block w-full appearance-none rounded-lg border-3 bg-[#FFFFFF]'+
   ' py-[calc(theme(spacing.2)-1px)] px-[calc(theme(spacing.3)-1px)] text-gray-900'+
   ' placeholder:text-gray-400 focus:border-cyan-500 focus:outline-none focus:ring-cyan-500 sm:text-sm'

function Label({ id, children, labelColor="text-[#785D87]" , isDynamic=false}) {
  return (
    <label
      htmlFor={id}
      className={clsx('ml-[3%] mb-2 block text-sm font-semibold',!isDynamic && labelColor)}
      
      style={{
        color:labelColor,
        transition: "color 1s ease-in-out"
      }}
    >
      {children}
    </label>
  )
}

export function TextField({ id, label, type = 'text', className,inputClassName,fieldBGColor, ...props }) {
  return (
    <div className={className}>
      {label && <Label id={id} labelColor={props.labelColor} isDynamic={props.isDynamic}>{label}</Label>}
      <input
        id={id} type={type} {...props}
        className={clsx(formClasses,!props.isDynamic&&inputClassName)}
        style={{
          color: props.labelColor,
          background:fieldBGColor,
          transition: "color 1s ease-in-out, background-color 1s ease-in-out"
        }}
         />
    </div>
  )
}

export function SelectField({ id, label, className, ...props }) {
  return (
    <div className={className}>
      {label && <Label id={id}>{label}</Label>}
      <select id={id} {...props} className={clsx(formClasses, 'pr-8')} />
    </div>
  )
}
