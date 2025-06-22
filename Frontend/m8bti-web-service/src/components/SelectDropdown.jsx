export default function SelectDropdown({dropdownName, handleDropdownClick, dropdownState, primaryColor, extraColor, options,
    handleSetDropdownValue, dropdownValue, variant = "default", ...props
 }) {
    return (
        <div className="mx-auto flex flex-col">
            <button onClick={handleDropdownClick} className="border-2 rounded-md"
                style={{
                    background: dropdownState ? primaryColor : extraColor,
                    color: dropdownState ? extraColor : primaryColor,
                    borderColor: primaryColor,
                    cursor: "pointer",
                    }}>
                 {variant==="default" &&(dropdownValue === ""||dropdownState ? dropdownName : dropdownValue)}
                 {variant==="tag" &&(dropdownValue === ""||dropdownState ? dropdownName : dropdownValue.name)}
            </button>
            <div
                className={`${
                dropdownState ? "" : "hidden"
                } overflow-y-auto mt-2 rounded-md border border-gray-300 shadow-md bg-white z-10 max-h-[50%]`}
                
            >
                {variant==="default" && options.map((option,id) => (
                    <div key={id}>
                        <button onClick={() => handleSetDropdownValue(option)}>
                            {option}
                        </button>
                    </div>
                ))}
                {variant==="tag" && options.map((option)=>(
                    <div key={option.id}>
                        <button onClick={() => handleSetDropdownValue(option)}>
                            {option.name}
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
}