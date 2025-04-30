import { useState, useEffect } from "react";

export default function TagPopUp({primaryColor,secondaryColor,extraColor,mbti,nickname,currentUser, userTags,
    ...props}){
        const [tags, setTags] = useState(userTags || []);
        const [tagCategories, setTagCategories] = useState([]);
        const [seletedCategory, setSelectedCategory] = useState("")
        const [selectedTag, setSelectedTag] = useState(null);
        const [catDropdownState, setCatDropdownState] = useState(false);
        const [catDropdownValue, setCatDropdownValue] = useState("");
        const [tagDropdownState, setTagDropdownState] = useState(false);
        const [tagDropdownValue, setTagDropdownValue] = useState("");
        

        const handleCatDropdownClick = () => {
            setCatDropdownState(!catDropdownState);
          };
        const handleSetCatDropdownValue = (value) => {
            setCatDropdownValue(value);
            setSelectedCategory(value);
            setCatDropdownState(!catDropdownState);
            console.log(seletedCategory)
        };
        const handleTagDropdownClick = () => {
            setTagDropdownState(!tagDropdownState);
        };
        const handleSetTagDropdownValue = (value) => {
            setTagDropdownValue(value);
            setSelectedTag(value);
            setTagDropdownState(!tagDropdownState);
            console.log(tagDropdownValue)
        };


        const fetchTagCategories=async()=>{
            try{
                const response = await fetch(`http://localhost:8080/api/tags/categories`, {
                    method: "GET",
                    credentials: "include"
                });
                if (!response.ok) throw new Error("Failed to fetch tag categories");
                const data = await response.json();
                setTagCategories(data);
            }
            catch{
                console.log("Error fetching tag categories")
            }
        }
        const fetchTags=async()=>{
            try{
                const response = await fetch(`http://localhost:8080/api/tags`, {
                    method: "GET",
                    credentials: "include"
                });
                if (!response.ok) throw new Error("Failed to fetch tags");
                const data = await response.json();
                setTags(data);
            }
            catch{
                console.log("Error fetching tags")
            }
        }

        useEffect(()=>{
            fetchTagCategories();
            fetchTags();
        },[])
        //console.log(tagCategories)
        //console.log(tags)
        

        return(
            <div>
                <div>{/* Categories Dropdown */}
                    <button onClick={handleCatDropdownClick}>
                        {catDropdownValue === ""||catDropdownState ? "Categories" : catDropdownValue}
                    </button>
                    <div
                        className={`${
                        catDropdownState ? "" : "hidden"
                        }`}
                    >
                        {tagCategories.map((category,id) => (
                            <div key={id}>
                                <button onClick={() => handleSetCatDropdownValue(category)}>
                                    {category}
                                </button>
                            </div>
                        ))}
                    </div>
                </div>
                <div>{/* Tags Dropdown */}
                    <button onClick={handleTagDropdownClick}>
                        {tagDropdownValue === ""||tagDropdownState ? "Tags" : tagDropdownValue.name}
                    </button>
                    <div
                        className={`${
                        tagDropdownState ? "" : "hidden"
                        }`}
                    >
                        {tags.map((tag) =>(
                            tag.category === seletedCategory &&(
                            <div key={tag.id}>
                                <button onClick={() => handleSetTagDropdownValue(tag)}>
                                    {tag.name}
                                </button>
                            </div>)
                        ))}
                    </div>
                </div>
            </div>
        )
    }