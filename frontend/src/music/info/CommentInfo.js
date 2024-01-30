import React from 'react';
import { Stack, Box} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';

import BoldText from '../../_global/components/text/BoldText';
import NormalText from '../../_global/components/text/NormalText';
import IconButton from '../../_global/components/button/IconButton';

const CommentInfo = () => {

    return (
        <Stack>
            <Box>
                <BoldText sx={{float: "left"}}>TestUser</BoldText>
                <BoldText sx={{float: "left", marginX: "5px"}}>·</BoldText>
                <BoldText sx={{float: "left", color: "lightgray"}}>2023-01-01</BoldText>

                <IconButton sx={{float: "right", marginLeft: "5px"}} buttonSx={{width: "20px", minWidth: "20px", height: "18px", minHeight: "18px"}} textSx={{fontSize: "12px", paddingBottom: "8px"}}>
                    <DeleteIcon sx={{width: "15px", height: "15px", float: "left"}}/>
                </IconButton>
                <IconButton sx={{float: "right", marginLeft: "5px"}} buttonSx={{width: "20px", minWidth: "20px", height: "18px", minHeight: "18px"}} textSx={{fontSize: "12px", paddingBottom: "8px"}}>
                    <EditIcon sx={{width: "15px", height: "15px", float: "left"}}/>
                </IconButton>
            </Box>
            <NormalText style={{fontWeight: "1px"}}>
                Test Comment
            </NormalText>
        </Stack>
    )
}

export default CommentInfo;