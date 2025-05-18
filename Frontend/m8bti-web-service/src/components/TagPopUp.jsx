import { useState, useEffect, use } from "react";
import { Button } from "./Button";
import axios from "axios";

export default function TagPopUp({primaryColor,secondaryColor,extraColor,mbti,nickname,currentUser, 
    userTags, setIsPopUpOpen, isPopUpOpen, onTagsUpdated, screenWidth, screenHeight,
    ...props}){
        const [tags, setTags] = useState([]);
        const [usersTags, setUsersTags] = useState(userTags || []);
        const [tagCategories, setTagCategories] = useState([]);
        const [seletedCategory, setSelectedCategory] = useState("")
        const [selectedTag, setSelectedTag] = useState(null);
        const [catDropdownState, setCatDropdownState] = useState(false);
        const [catDropdownValue, setCatDropdownValue] = useState("");
        const [tagDropdownState, setTagDropdownState] = useState(false);
        const [tagDropdownValue, setTagDropdownValue] = useState("");
        const [hoveredTag, setHoveredTag] = useState(null);

        

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

        const addTag=()=>{
            if(selectedTag===null||selectedTag===undefined||seletedCategory==="")
                return;
            setUsersTags(usersTags.concat(selectedTag))
            setTagDropdownValue("");
            setSelectedTag(null);
            setCatDropdownValue("");
            //console.log(usersTags)
        }


        const removeTag=(tag)=>{
            setUsersTags(usersTags.filter((t) => t.id !== tag.id));
            setHoveredTag(null);
            console.log(usersTags)
        }


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

        const handleTagsSave=async()=>{
            try{
                console.log(usersTags)
                console.log(usersTags.map((tag) => (tag.id)))
                const response = await axios.put("http://localhost:8080/api/user/me", {
                    tagIds: usersTags.map((tag) => (tag.id))
                },
                {
                    withCredentials: true,
                });
                console.log(response.status);
                console.log("Tags updated:", response.data);
                if (response.status === 200) {
                    console.log("Tags successfully updated!");
                    onTagsUpdated(usersTags);
                    setIsPopUpOpen(false);

                }
                //if (!response.ok) throw new Error("Failed to save tags");
                //const data = await response.json();
                //console.log(data)
            }
            catch(error)
            {
                console.log("Error saving tags: ", error);
            }
        }

        useEffect(()=>{
            fetchTagCategories();
            fetchTags();
        },[])
        useEffect(()=>{
            setUsersTags(userTags)
        }, [isPopUpOpen])
        
        useEffect(()=>{
            console.log(usersTags)
        }, [usersTags])
        
        //console.log(tagCategories)
        //console.log(tags)
        useEffect(()=>{
            if(userTags!==null&&userTags!==undefined){
                setUsersTags(userTags)
                setTagDropdownValue("");
                setSelectedTag(null);
                setCatDropdownValue("");
                setSelectedCategory("");
            }
        }, [userTags])
        console.log(userTags||"No tags")

        return(
            <div className="w-screen h-screen fixed top-0 left-0 z-50 items-center"
            style={{
                background: 'rgba(128,128,128,0.7)'
            }}
            onClick={(e) => {
                if (e.target === e.currentTarget) {
                    setIsPopUpOpen(false);
                }
            }}
            >
                <div className="w-[50%] min-h-[50%] h-[50%] mx-auto my-[15%] rounded-lg border-2 border-gray-300 shadow-lg"
                        style={{
                            background: extraColor,

                        }}
                >
                    <div className="grid-cols-2 grid min-h-[40%] max-h-[40%] pt-[2%]">
                        {/* Categories Dropdown */}
                        <div className="mx-auto flex flex-col">
                            <button onClick={handleCatDropdownClick} className="border-2 rounded-md"
                                style={{
                                    background: catDropdownState ? primaryColor : extraColor,
                                    color: catDropdownState ? extraColor : primaryColor,
                                    borderColor: primaryColor,
                                    cursor: "pointer",
                                    }}>
                                {catDropdownValue === ""||catDropdownState ? "Categories" : catDropdownValue}
                            </button>
                            <div
                                className={`${
                                catDropdownState ? "" : "hidden"
                                } overflow-y-auto mt-2 rounded-md border border-gray-300 shadow-md bg-white z-10 max-h-[50%]`}
                                
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
                        {/* Tags Dropdown */}
                        <div className="mx-auto flex flex-col">
                            <button onClick={handleTagDropdownClick} className="border-2 rounded-md"
                            style={{
                                    background: tagDropdownState ? primaryColor : extraColor,
                                    color: tagDropdownState ? extraColor : primaryColor,
                                    borderColor: primaryColor,
                                    cursor: "pointer",
                                    }}>
                                {tagDropdownValue === ""||tagDropdownState ? "Tags" : tagDropdownValue.name}
                            </button>
                            <div
                                className={`${
                                tagDropdownState ? "" : "hidden"
                                } overflow-y-auto mt-2 rounded-md border border-gray-300 shadow-md bg-white max-h-[50%]`}
                            >
                                {tags.map((tag) =>(
                                    tag.category === seletedCategory && !usersTags.some(userTag=>userTag.id===tag.id) &&(
                                    <div key={tag.id}>
                                        <button onClick={() => handleSetTagDropdownValue(tag)}>
                                            {tag.name}
                                        </button>
                                    </div>)
                                ))}
                            </div>
                        </div>
                    </div>
                    <div>
                        <div>
                            {usersTags!==null&&usersTags!==undefined&&usersTags.length>0&&(
                                <div className="flex flex-row flex-wrap items-center px-[5%] pt-[2%]">
                                    {usersTags.map((tag, id) => (
                                        <div 
                                            className="w-max-[10%] mx-[1%] mt-[1%] rounded-lg border-2 px-[1%] py-[0.2%]" key={id}
                                            
                                            onMouseEnter={() => setHoveredTag(id)}
                                            onMouseLeave={() => setHoveredTag(null)}
                                            onClick={() => removeTag(tag)}
                                            
                                            style={{
                                                backgroundColor: hoveredTag === id&&currentUser===nickname ? primaryColor : extraColor,
                                                color: hoveredTag === id&&currentUser===nickname ? extraColor : primaryColor,
                                                borderColor: primaryColor,
                                                cursor: "pointer",
                                            }}
                                        >
                                            <span className={hoveredTag===id?"":"hidden"}>X </span>{tag.name}
                                        </div>
                                    ))}
                                </div>
                        )}
                    </div>
                    <div className="flex flex-row flex-wrap items-center justify-between px-[5%] pt-[2%]">
                    <Button color={mbti} onClick={addTag}> Add Tag</Button>
                    <Button color={mbti} onClick={handleTagsSave}> Save Tags</Button>
                    </div>
                </div>
                </div>
            </div>
        )
    }