export function MBTIColors({colorDest, mbti}){
    const colorsPrimary=["#785D87","#33A474","#4298B4","#E4AE3A"]
    const colorsSecondary=["#E7DFEA", "#D6ECE3","#D9EAF0","#F9EED7"]
    
    if(colorDest=="Primary"){
        //console.log("Color",colorsPrimary[mbti])
        //console.log("I'm in");
        if(mbti===5)
            return colorsPrimary
        return colorsPrimary[mbti]
    }
    else if(colorDest=="Secondary"){
        //console.log("Index", mbti)
        //console.log("Color",colorsSecondary[mbti])
        if(mbti===5)
            return colorsSecondary
        return colorsSecondary[mbti]
    }
}