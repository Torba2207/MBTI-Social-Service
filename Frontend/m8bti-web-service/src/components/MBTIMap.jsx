import React from "react";

export function getMBTIGroupIndex(mbti) {
  if (["INTJ", "INTP", "ENTJ", "ENTP"].includes(mbti)) return 0;
  if (["INFJ", "INFP", "ENFJ", "ENFP"].includes(mbti)) return 1;
  if (["ISTJ", "ISFJ", "ESTJ", "ESFJ"].includes(mbti)) return 2;
  if (["ISTP", "ISFP", "ESTP", "ESFP"].includes(mbti)) return 3;
  return -1; // unknown or invalid type
};