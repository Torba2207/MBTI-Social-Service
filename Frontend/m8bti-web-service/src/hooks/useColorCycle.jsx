import { useState, useEffect } from "react";

function useColorCycle(colors, interval = 1000) {
  const [index, setIndex] = useState(0);

  useEffect(() => {
    const timer = setInterval(() => {
      setIndex((prevIndex) => (prevIndex + 1) % colors.length);
    }, interval);

    return () => clearInterval(timer);
  }, [colors, interval]);

  return colors[index];
}

export default useColorCycle;
