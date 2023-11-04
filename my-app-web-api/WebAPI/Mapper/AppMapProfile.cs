using AutoMapper;
using WebAPI.data.Entities;
using WebAPI.Models.Category;

namespace WebAPI.Mapper
{
    public class AppMapProfile : Profile
    {
        public AppMapProfile()
        {
            CreateMap<CategoryEntity, CategoryItemViewModel>();
            CreateMap<CategoryCreateViewModel, CategoryEntity>();
        }
    }
}
