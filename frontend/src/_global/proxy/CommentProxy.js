import axios from 'axios';

class CommentProxy {
    static async createComment(musicId, content, jwtTokenState) {
        console.log(`[EFFECT] createComment : <musicId:${musicId}, content:${content}>`)

        const requestHeader = {headers: {Authorization: jwtTokenState.jwtToken.Authorization}};
        const reqDto = {
            "musicId": musicId,
            "content": content
        }
        const response = await axios.put(`http://${window.location.host}/api/comment/comments/createComment`, reqDto, requestHeader);
        
        console.log(response)
    }


    static async searchCommentAll(jwtTokenState) {
        console.log(`[EFFECT] searchCommentAll`)

        const requestHeader = {headers: {Authorization: jwtTokenState.jwtToken.Authorization}};
        const response = await axios.get(`http://${window.location.host}/api/collectedData/comments`, requestHeader);
        
        console.log(response)
        return response.data._embedded.comments
    }
}

export default CommentProxy