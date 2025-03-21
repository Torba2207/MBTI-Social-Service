import clsx from 'clsx'

const formClasses =
  'block w-full appearance-none rounded-lg border-3 border-[#785D87] bg-[#EEE7F]'+
   ' py-[calc(theme(spacing.2)-1px)] px-[calc(theme(spacing.3)-1px)] text-gray-900'+
   ' placeholder:text-gray-400 focus:border-cyan-500 focus:outline-none focus:ring-cyan-500 sm:text-sm'

function Label({ id, children, labelColor="text-[#785D87]" }) {
  return (
    <label
      htmlFor={id}
      className={clsx('ml-[3%] mb-2 block text-sm font-semibold',labelColor)}
    >
      {children}
    </label>
  )
}

export function TextField({ id, label, type = 'text', className, ...props }) {
  return (
    <div className={className}>
      {label && <Label id={id} labelColor={props.labeColor}>{label}</Label>}
      <input id={id} type={type} {...props} className={formClasses} />
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
