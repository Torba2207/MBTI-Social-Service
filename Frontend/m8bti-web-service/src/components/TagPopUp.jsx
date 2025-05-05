import { useState, useEffect } from "react";
import { Button } from "./Button";
import axios from "axios";

export default function TagPopUp({primaryColor,secondaryColor,extraColor,mbti,nickname,currentUser, userTags, setIsPopUpOpen, onTagsUpdated,
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
                <div className="w-[50%] h-[50%] mx-auto mt-[10%] rounded-lg border-2 border-gray-300 shadow-lg"
                        style={{
                            background: extraColor
                        }}
                >
                    {/* Categories Dropdown */}
                    <div>
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
                    {/* Tags Dropdown */}
                    <div>
                        <button onClick={handleTagDropdownClick}>
                            {tagDropdownValue === ""||tagDropdownState ? "Tags" : tagDropdownValue.name}
                        </button>
                        <div
                            className={`${
                            tagDropdownState ? "" : "hidden"
                            }`}
                        >
                            {tags.map((tag) =>(
                                tag.category === seletedCategory && !userTags.some(userTag=>userTag.id===tag.id) &&(
                                <div key={tag.id}>
                                    <button onClick={() => handleSetTagDropdownValue(tag)}>
                                        {tag.name}
                                    </button>
                                </div>)
                            ))}
                        </div>
                    </div>
                    <Button color={mbti} onClick={addTag}> Add Tag</Button>
                    <Button color={mbti} onClick={handleTagsSave}> Save Tags</Button>
                </div>
            </div>
        )
    }