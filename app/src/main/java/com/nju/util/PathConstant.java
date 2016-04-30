package com.nju.util;

public interface PathConstant {

    String BASE_URL = "http://115.159.186.158:8080/school-friend-service-webapp/service/api/";
    String AUTHOR_PATH = "author/";
    String AUTHOR_SUB_PATH_REGISTER = "register";
    String AUTHOR_SUB_PATH_UPDATE_HEAD_ICON = "updateHeadIcon";
    String AUTHOR_SUB_PATH_GET_AUTHOR_BY_ID = "getAuthorById";
    String AUTHOR_SUB_PATH_GET_AUTHOR_BY_USER_PASS = "getAuthorByNameAndPass";
    String AUTHOR_SUB_PATH_GET_IMAGE = "getAuthorImage";

    String ALUMNI_TALK_PATH = "alumniTalk/";
    String ALUMNI_TALK_SUB_PATH_SAVE = "save";
    String ALUMNI_TALK_SUB_PATH_CANCEL = "cancel";
    String ALUMNI_TALK_SUB_PATH_GET_CONTENTS_BY_AUTHOR = "getContentsByAuthor";
    String ALUMNI_TALK_SUB_PATH_VIEW_ALL = "viewTalks";
    String ALUMNI_TALK_SUB_PATH_VIEW_OWN_TALKS = "viewOwnTalks";
    String ALUMNI_TALK_SUB_PATH_VIEW_TALKS_BY_COLLEGE = "viewTalksByCollege";
    String ALUMNI_TALK_SUB_PATH_VIEW_TALKS_BY_YEAR = "viewTalksByYear";
    String ALUMNI_TALK_SUB_PATH_VIEW_MOST_TALKS = "viewMostTalks";
    String ALUMNI_TALK_SUB_PATH_VIEW_GET_PRAISE = "getTalkPraisesByIds";
    String ALUMNI_TALK_SUB_PATH_VIEW_GET_COMMENTS = "getTalkCommentsByIds";

    String ALUMNI_TALK_COMMENT_PATH = "alumniTalkComment/";
    String ALUMNI_TALK_COMMENT_SUB_PATH_SAVE = "save";
    String ALUMNI_TALK_COMMENT_SUB_PATH_CANCEL = "cancel";

    String ALUMNI_TALK_PRAISE_PATH = "alumniTalkPraise/";
    String ALUMNI_TALK_PRAISE_SUB_PATH_SAVE = "save";
    String ALUMNI_TALK_PRAISE_SUB_PATCH_CANCEL = "cancel";

    String USER_DEGREE_INFO_PATH = "userDegreeInfo/";
    String USER_DEGREE_INFO_SUB_PATH_SAVE = "save";

    String AUTHORIZATION_PATH = "authorization/";
    String AUTHORIZATION_SUB_PATH_XUE_XIN_NET = "xueXinNet";

    String FRIEND_ALUMIC_PATH = "friendAlumic/";
    String FRIEND_ALUMIC_SUB_PATH = "getAlumics";

    String PERSONAL_ALUMIC_PATH = "personalAlumic/";
    String PERSONAL_ALUMIC_SUB_PATH_GET_ALUMIC = "getAlumics";

    String RECOMMEND_WORK_PATH = "recommendWork/";
    String RECOMMEND_WORK_SUB_PATH_SAVE = "save";
    String RECOMMEND_WORK_SUB_PATH_CANCEL = "cancel";
    String RECOMMEND_WORK_SUB_PATH_QUERY = "query";
    String RECOMMEND_WORK_SUB_PATH_ASK = "ask";
    String RECOMMEND_WORK_SUB_PATH_VIEW_OWN = "viewOwnRecommendWork";
    String RECOMMEND_WORK_SUB_PATH_BY_TYPE = "viewRecommendWorkByType";
    String RECOMMEND_WORK_SUB_PATH_BY_COLLEGE = "viewRecommendWorkByCollege";
    String RECOMMEND_WORK_SUB_PATH_BY_TYPE_COLLEGE = "viewRecommendWorkByTypeCollege";
    String RECOMMED_WORK_SUB_PATH_GET_ASKS = "getRecommendAsksByIds";

    String ALUMNS_VOICE_PATH = "alumnsVoice/";
    String ALUMNS_VOICE_SUB_PATH_SAVE = "save";
    String ALUMNS_VOICE_SUB_PATH_CANCEL = "cancel";
    String ALUMNIS_VOICE_SUB_PATH_QUERY = "query";
    String ALUMNS_VOICE_SUB_PATH_GET_ALL_VOICE = "getAllVoices";
    String ALUMNS_VOICE_SUB_PATH_VIEW_OWN_VOICE = "viewOwnVoices";
    String ALUMNS_VOICE_SUB_PATH_VIEW_BY_TYPE = "getAlumnusVoicesByTypes";
    String ALUMNS_VOICE_SUB_PATH_VIEW_BY_COLLEGE = "getAlumnusVoicesByCollege";
    String ALUMNS_VOICE_SUB_PATH_VIEW_BY_COLLEGE_TYPE = "getAlumnusVoicesByTypesAndCollege";
    String ALUMNS_VOICE_SUB_PATH_VIEW_GET_PRAISE = "getPraisesByVoiceId";
    String ALUMNS_VOICE_SUB_PATH_VIEW_GET_COMMENT = "getCommentsByVoiceId";
    String ALUMNS_VOICE_SUB_PATH_GET_LABELS = "getLabels";

    String ALUMNUS_VOICE_COMMENT_PATH = "alumnusVoiceComment/";
    String ALUMNUS_VOICE_COMMENT_SUB_PATH_SAVE = "save";
    String ALUMNUS_VOICE_COMMENT_SUB_PATH_CANCEL = "cancel";

    String ALUMNUS_VOICE_PRAISE_PATH = "alumnusVoicePraise/";
    String ALUMNUS_VOICE_PRAISE_SUB_PATH_SAVE = "save";
    String ALUMNUS_VOICE_PRAISE_SUB_PATH_CANCEL = "cancel";

    String ALUMNIS_QUESTION_PATH = "alumniQuestion/";
    String ALUMNIS_QUESTION_SUB_PATH_SAVE = "save";
    String ALUMNIS_QUESTION_SUB_PATH_CANCEL = "cancel";
    String ALUMNIS_QUESTION_SUB_PATH_CLOSE = "close";
    String ALUMNIS_QUESTION_SUB_PATH_VIEW = "viewAlumniQuestions";
    String ALUMNIS_QUESTION_SUB_PATH_VIEW_OWN = "viewOwnAlumniQuestions";
    String ALUMNIS_QUESTION_SUB_PATH_VIEW_COLLEGE = "viewAlumniQuestionsByCollege";
    String ALUMNIS_QUESTION_SUB_PATH_VIEW_YEAR = "viewAlumniQuestionsByYear";
    String ALUMNIS_QUESTION_SUB_PATH_VIEW_LABEL = "viewAlumniQuestionsByLabel";
    String ALUMNIS_QUESTION_SUB_PATH_GET_COMMENTS = "getQuestionAnswers";
    String ALUMNIS_QUESTION_SUB_PATH_GET_LABELS = "getLabels";


    String ALUMNIS_QUESTION_ANSWER_PATH = "alumniQuestionAnswer/";
    String ALUMNIS_QUESTION_ANSWER_SUB_PATH_SAVE = "save";
    String ALUMNIS_QUESTION_ANSWER_SUB_PATH_CANCEL = "cancel";

    String IMAGE_PATH = BASE_URL +"images/";
    String SMALL = "small";
    String IMAGE_PATH_SMALL = BASE_URL +"images/"+SMALL+"/";
    String ALUMNI_TALK_IMG_PATH = "alumniTalk/";
    String ALUMNI_QUESTION_IMG_PATH = "alumniQuestion/";
    String ALUMNI_VOICE_IMG_PATH = "alumnusVoice/";
    String ALUMNI_RECOMMEND_IMG_PATH ="recommendWork/";
    String HEAD_ICON_IMG = "headIconImgPath/";
    String BG_IMAGE = "bgImgPath/";

    String REGISTER_PATH = "register";
    String REGISTER_SUB_PATH_PHONE = "phoneNumber";
    String REGISTER_SUB_PATH_PHONE_VALIDATE = "phoneValidate";

    String COLLEGES_PATH = "college/";
    String GET_SCHOOL = "getSchools";

    String DYNAMIC_COLLECT = "dynamicCollect/";
    String SAVE_DYNAMIC_COLLECT = "save";
    String DELETE_DYNAMIC_COLLECT = "delete";
    String GET_DYNAMIC_COLLECT = "getDynamicCollects";

    String VOICE_COLLECT = "voiceCollect/";
    String SAVE_VOICE_COLLECT = "save";
    String DELETE_VOICE_COLLECT = "delete";
    String GET_VOICE_COLLECT = "getVoiceCollects";

    String QUESTION_COLLECT = "questionCollect/";
    String SAVE_QUESTION_COLLECT = "save";
    String DELETE_QUESTION_COLLECT = "delete";
    String GET_QUESTION_COLLECT = "getQuestionCollects";

    String RECOMMEND_COLLECT = "recommendCollect/";
    String SAVE_RECOMMEND_COLLECT = "save";
    String DELETE_RECOMMEND_COLLECT = "delete";
    String GET_RECOMMEND_COLLECT = "getRecommendCollects";

}
