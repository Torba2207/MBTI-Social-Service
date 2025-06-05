const sendFriendRequest=async(reciever)=> {
    try{
        const response=await fetch(`http://localhost:8080/api/friendships/me/${reciever}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(),
        credentials: 'include'
    })
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        console.log('Friend request sent successfully:', data);
        console.log('Friend request sent successfully to:', reciever);
        console.log('Response status:', response.status);
    }
    catch(error){
        console.error('There was a problem with the fetch operation:', error);
    };
}

export default sendFriendRequest;